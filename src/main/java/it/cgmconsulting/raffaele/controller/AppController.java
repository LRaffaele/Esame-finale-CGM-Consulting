package it.cgmconsulting.raffaele.controller;

import it.cgmconsulting.raffaele.payload.request.FilmRequest;
import it.cgmconsulting.raffaele.payload.request.RentalAddOrUpdateRequest;
import it.cgmconsulting.raffaele.service.FilmService;
import it.cgmconsulting.raffaele.service.InventoryService;
import it.cgmconsulting.raffaele.service.RentalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
public class AppController {

    private final FilmService filmService;
    private final InventoryService inventoryService;
    private final RentalService rentalService;

    /*
     * 1. @PatchMapping("/update-film/{filmId}") ---> @RequestBody FilmRequest
     */

    @PatchMapping("/update-film/{filmId}")
    public ResponseEntity<?> updateFilm (@PathVariable @Min(1) long filmId, @RequestBody @Valid FilmRequest request){
        return filmService.updateFilm(filmId, request);
    }

    /*
    * 2. @GetMapping("/find-films-by-language/{languageId}") --> List<FilmResponse>
    *  film_id, title, description, release_year, language_name
    */

    @GetMapping("/find-films-by-language/{languageId}")
    public ResponseEntity<?> findFilmsByLanguage(@PathVariable @Min(1) long languageId){
        return filmService.findFilmsByLanguageId(languageId);
    }


    /* 3. @PutMapping("/add-film-to-store/{storeId}/{filmId}") */

    @PutMapping("/add-film-to-store/{storeId}/{filmId}")
    public ResponseEntity<?> addFilmToStore (@PathVariable @Min(1) long storeId, @PathVariable @Min(1) long filmId){
        return inventoryService.addFilmToStore(storeId, filmId);
    }

    /* 4. @GetMapping("/count-customers-by-store/{storeName}") -->
     *    CustomerStoreResponse: store_name, total_customers (N.B. sono da
     *    considerare 'clienti' di un determinato store tutti quelli che hanno
     *    effettuato almeno un noleggio)
     */

    @GetMapping("/count-customers-by-store/{storeName}")
    public ResponseEntity<?> countCustomerByStore(@PathVariable @NotBlank @Size(max = 60) String storeName){
        return rentalService.countCustomerByStore(storeName);
    }

    /*5.  @PutMapping("/add-update-rental") add: inserimento rental; update:
     *    aggiornamento solo della data restituzione (rental_return). In questo
     *    caso non vi sono suggerimenti sui parametri da passare al controller. N.B.
     *    Il film è noleggiabile solo se almeno una copia è presente in uno store e
     *    non risulta a sua volta già noleggiata.
     */

    @PutMapping("/add-update-rental")
    public ResponseEntity<?> addOrUpdateRental(@RequestBody @Valid RentalAddOrUpdateRequest request){
        return rentalService.addOrUpdateRental(request);
    }

    /* 6. @GetMapping("/count-rentals-in-date-range-by-store/{storeId}")
     *    @RequestParam Date start,Date end --> return: conteggio dei noleggi in un
     *    determinato store in un determinato arco temporale (comprende anche i film
     *    non ancora restituiti)
     */

    @GetMapping("/count-rentals-in-date-range-by-store/{storeId}")
    public ResponseEntity<?> countRentalsInDateRange (@PathVariable long storeId, @RequestParam @PastOrPresent LocalDate start, @PastOrPresent @RequestParam LocalDate end){
        return rentalService.countRentalsInDateRange(storeId, start, end);
    }

    /* 7.   @GetMapping("/find-all-films-rent-by-one-customer/{customerId}") return:
            List<FilmRentResponse> : film_id, title, store_name (considerare anche i
            film non ancora restituiti).
     */

    @GetMapping("/find-all-films-rent-by-one-customer/{customerId}")
    public ResponseEntity<?> findAllFilmsRentedByOneCustomer(@PathVariable @Min(1) long customerId){
        return rentalService.findAllRentalsByCustomerId(customerId);
    }

    /* 8.   @GetMapping("/find-film-with-max-number-of-rent") Trovare il film o i film
            con il maggior numero di noleggi (anche se non ancora restituiti).
            List<FilmMaxRentResponse> : film_id, title, numero di noleggi totale.
     */

    @GetMapping("/find-film-with-max-number-of-rent")
    public ResponseEntity<?> findFilmWithMaxNumberOfRent(){
        return rentalService.findFilmWithMaxNumberOfRent();
    }

    /* 9.   @GetMapping("/find-films-by-actors") ---> @RequestParam: Collection di
            staff_id di attori --> return List<FilmResponse>: questa lista dovrà
            contenere i film a cui hanno lavorato TUTTI INSIEME gli attori i cui
            identificativi sono stati passati come parametro. La lista dovrà essere
            ordinata alfabeticamente per titolo del film. */

    @GetMapping("/find-films-by-actors")
    public ResponseEntity<?> findFilmByActors(@RequestParam Set<Long> staffIds){
        return filmService.findFilmsByCast(staffIds);
    }

    /* 10.  @GetMapping("/find-rentable-films") ---> @RequestParam title ---> return
            List<FilmRentableResponse>: title, store_name, numero totale di copie in
            possesso del negozio, numero di copie disponibili */

    @GetMapping("/find-rentable-films")
    public ResponseEntity<?> findRentableFilms(@RequestParam String title){
        return rentalService.findRentableFilms(title);
    }


}
