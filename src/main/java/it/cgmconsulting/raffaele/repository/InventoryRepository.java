package it.cgmconsulting.raffaele.repository;

import it.cgmconsulting.raffaele.entity.Inventory;
import it.cgmconsulting.raffaele.payload.response.FilmRentableResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // trovo gli inventoryId disponibili al noleggio escludendo dal risultato gli inventoryId presenti nelle rental ma ancora non restituiti (rentalReturn == null)
    @Query(value = " ( SELECT i.inventory_id " +
            "FROM inventory i " +
            "LEFT JOIN rental r ON i.inventory_id = r.inventory_id " +
            "WHERE i.store_id = :store AND i.film_id = :film ) " +
            "EXCEPT " +
            "( SELECT rent.inventory_id FROM rental rent WHERE rent.rental_return IS NULL ) " , nativeQuery = true)
    List<Long> findRentableByFilmAndStore(long film, long store);

    @Query(value = "SELECT new it.cgmconsulting.raffaele.payload.response.FilmRentableResponse( " +
            "i.film.title, " +
            "i.store.storeName, " +
            "COUNT (i.inventoryId), " + // conteggio totale degli inventory di quel film e di quel negozio
            "(SELECT COUNT (i.inventoryId) - COUNT(k.inventoryId) " + // sottraggo agli inventory totali quelli che sono presenti sulla tabella Rental e che hanno un record con rentalReturn == null
                                            "FROM Inventory k LEFT JOIN Rental r ON k.inventoryId = r.rentalId.inventory.inventoryId " +
                                            "WHERE i.inventoryId = k.inventoryId AND r.rentalReturn IS NULL AND r.rentalId.rentalDate IS NOT NULL )" + // rentalDate IS NOT NULL perchè altrimenti conterei per via della LEFT JOIN gli inventory che
                                                                                                                                                        // sono presenti sulla tabella Inventory ma non sulla tabella Rental (e che quindi sono disponibili perchè mai stati presi in prestito)
            ") FROM Inventory i " +
            "WHERE i.film.title = :title " +
            "GROUP BY i.store.storeId  ")
    List<FilmRentableResponse> findRentableFilmByTitle(String title);

}
