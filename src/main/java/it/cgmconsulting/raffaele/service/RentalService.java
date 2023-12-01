package it.cgmconsulting.raffaele.service;

import it.cgmconsulting.raffaele.entity.*;
import it.cgmconsulting.raffaele.exception.ResourceNotFoundException;
import it.cgmconsulting.raffaele.payload.request.RentalAddOrUpdateRequest;
import it.cgmconsulting.raffaele.payload.response.CustomerStoreResponse;
import it.cgmconsulting.raffaele.payload.response.FilmMaxRentResponse;
import it.cgmconsulting.raffaele.payload.response.FilmRentResponse;
import it.cgmconsulting.raffaele.payload.response.FilmRentableResponse;
import it.cgmconsulting.raffaele.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;
    private final FilmRepository filmRepository;
    private final InventoryRepository inventoryRepository;
    private final StoreRepository storeRepository;
    private final FilmService filmService;
    private final InventoryService inventoryService;

    public ResponseEntity<?> countCustomerByStore(String storeName) {

        // controllo che lo store esista sul db
        if(!storeRepository.existsByStoreName(storeName))
            return new ResponseEntity<>("Store " + storeName + " not found", HttpStatus.NOT_FOUND);

        CustomerStoreResponse customerStoreResponse = rentalRepository.countCustomersByStore(storeName);
        return new ResponseEntity(customerStoreResponse, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> addOrUpdateRental(RentalAddOrUpdateRequest request) {

        // verifico che ai dati della request corrispondano dei record sul db
        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "customerId", request.getCustomerId())
        );

        Film film = filmService.getFilmEntityFromId(request.getFilmId());

        Store store = inventoryService.getStoreEntityFromId(request.getStoreId());


        // request.getRentalReturn non è valorizzato ---> è una richiesta di AddRental
        if(request.getRentalReturn() == null ) {
            // cerco la lista degli inventoryId disponibili al noleggio nello store indicato
            List<Long> availableInventoriesIds = inventoryRepository.findRentableByFilmAndStore(request.getFilmId(), request.getStoreId());

            if (availableInventoriesIds.isEmpty()) // se la lista è vuota non ci sono copie di questo film disponibili nello store
                return new ResponseEntity<>("There is no available copy of film with id " + request.getFilmId() + " in store " + request.getStoreId(), HttpStatus.NOT_FOUND);

            // availableInventoriesIds.get(0) -> prendo il primo elemento della lista visto che ai fini della add un inventory vale l'altro
            Optional<Inventory> selectedInventory = inventoryRepository.findById(availableInventoriesIds.get(0));

            if (selectedInventory.isEmpty()) // arrivati qui selectedInventory non dovrebbe essere empty, controllo in ogni caso per sicurezza
                return new ResponseEntity<>("Something went wrong while searching for inventory", HttpStatus.INTERNAL_SERVER_ERROR);


            Rental newRental = new Rental(new RentalId(customer, selectedInventory.get(), LocalDateTime.now()), null);
            rentalRepository.save(newRental);

            return new ResponseEntity<>(newRental, HttpStatus.OK);
        }

        // request.getRentalReturn è valorizzato --> è una richiesta di updateRental, voglio restituire un film

        if(request.getRentalDate() == null)  // Controllo se è stata inserita la rentalDate del noleggio. Senza di essa non posso identificare univocamente l'inventory da restituire, l'utente potrebbe aver noleggiato 2 volte lo stesso film dallo stesso store
            return new ResponseEntity<>("Missing rental date, please insert the date of your rental if you want to return it", HttpStatus.BAD_REQUEST);


        //recupero il rental verificando che esista sul db
        Optional<Rental> rentalForUpdate = rentalRepository.findRentalsForUpdateWithRentalDate(customer, film, store, request.getRentalDate());

        if(rentalForUpdate.isEmpty())
            return new ResponseEntity<>("Selected customer has not rented this movie in this store or date of rental is incorrect", HttpStatus.NOT_FOUND);


        rentalForUpdate.get().setRentalReturn(request.getRentalReturn()); // valorizzo la rentalReturn con quella della request
        return new ResponseEntity<>(rentalForUpdate.get(), HttpStatus.OK);
    }

    public ResponseEntity<?> countRentalsInDateRange(long storeId, LocalDate start, LocalDate end) {
        // controllo che lo store esista
        if(!storeRepository.existsById(storeId))
            return new ResponseEntity<>("Store with id "+ storeId + " not found", HttpStatus.NOT_FOUND);

        // nella query passo la data iniziale alle 00:00 e la data finale alle 23:59 in modo da comprendere i giorni nella loro interezza
        long totalRentals = rentalRepository.countRentalsInDateRange(storeId, start.atTime(0,0,0), end.atTime(23,59,59));
        return new ResponseEntity<>("Selected store rented " + totalRentals + " items between " + start + " and " + end, HttpStatus.OK);
    }

    public ResponseEntity<?> findAllRentalsByCustomerId(long customerId) {
        //controllo che il customer esista
        if(!customerRepository.existsById(customerId))
            return new ResponseEntity<>("Customer with id " + customerId + " not found", HttpStatus.NOT_FOUND);

        List<FilmRentResponse> filmRentResponses = rentalRepository.getAllFilmRentResponses(customerId);
        return new ResponseEntity<>(filmRentResponses, HttpStatus.OK);
    }

    public ResponseEntity<?> findFilmWithMaxNumberOfRent() {

        List<FilmMaxRentResponse> fullList = rentalRepository.findFilmByNumberOfRent();  // ottengo una lista ordinata per totalRents DESC

        List<FilmMaxRentResponse> response = new ArrayList<>(); // aggiungo il primo elemento alla lista di response e controllo se esistono altri film con gli stessi totalRents
        response.add(fullList.get(0));
        for(int i = 1; i < fullList.size(); i++){
            if(fullList.get(i).getTotalRents() == response.get(0).getTotalRents())
                response.add(fullList.get(i));
            else
                break;
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> findRentableFilms(String title) {

        if(!filmRepository.existsByTitle(title)) // controllo se il film esiste sul db
            return new ResponseEntity<>("Unknown film selected", HttpStatus.BAD_REQUEST);

        List<FilmRentableResponse> rentableFilms = inventoryRepository.findRentableFilmByTitle(title);
        return new ResponseEntity<>(rentableFilms, HttpStatus.OK);
    }

}
