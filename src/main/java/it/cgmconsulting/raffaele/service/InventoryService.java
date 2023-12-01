package it.cgmconsulting.raffaele.service;

import it.cgmconsulting.raffaele.entity.Film;
import it.cgmconsulting.raffaele.entity.Inventory;
import it.cgmconsulting.raffaele.entity.Store;
import it.cgmconsulting.raffaele.exception.ResourceNotFoundException;
import it.cgmconsulting.raffaele.repository.InventoryRepository;
import it.cgmconsulting.raffaele.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final StoreRepository storeRepository;
    private final FilmService filmService;

    public ResponseEntity<?> addFilmToStore(long storeId, long filmId) {

        // controllo che film e store siano presenti sul db
        Store store = getStoreEntityFromId(storeId);

        Film film = filmService.getFilmEntityFromId(filmId);

        // creo un nuovo record e lo persisto sul db
        Inventory inventory = new Inventory(store, film);
        inventoryRepository.save(inventory);

        return new ResponseEntity<>("Film " + film.getTitle() +  " successfully added to " + store.getStoreName(), HttpStatus.CREATED);
    }

    public Store getStoreEntityFromId(long storeId){
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResourceNotFoundException("Store", "storeId", storeId)
        );
        return store;
    }

}
