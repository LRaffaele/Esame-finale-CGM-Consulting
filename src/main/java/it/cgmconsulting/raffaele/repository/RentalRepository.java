package it.cgmconsulting.raffaele.repository;

import it.cgmconsulting.raffaele.entity.*;
import it.cgmconsulting.raffaele.payload.response.CustomerStoreResponse;
import it.cgmconsulting.raffaele.payload.response.FilmMaxRentResponse;
import it.cgmconsulting.raffaele.payload.response.FilmRentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface RentalRepository extends JpaRepository<Rental, RentalId> {

    @Query(value = "SELECT new it.cgmconsulting.raffaele.payload.response.CustomerStoreResponse(" +
            "r.rentalId.inventory.store.storeName, " +
            "(COUNT (DISTINCT r.rentalId.customer.customerId) ) " +  //DISTINCT per non contare più volte lo stesso customer
            ") FROM Rental r " +
            "WHERE r.rentalId.inventory.store.storeName = :storeName" )
    CustomerStoreResponse countCustomersByStore(String storeName);

    @Query(value = "SELECT COUNT (r.rentalId) " +
            "FROM Rental r " +
            "WHERE r.rentalId.inventory.store.storeId = :storeId " +
            "AND (r.rentalId.rentalDate >= :start AND r.rentalId.rentalDate <= :end) ")
    long countRentalsInDateRange(long storeId, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT DISTINCT new it.cgmconsulting.raffaele.payload.response.FilmRentResponse(" +
            "r.rentalId.inventory.film.filmId, " +
            "r.rentalId.inventory.film.title, " +
            "r.rentalId.inventory.store.storeName " +
            ") FROM Rental r " +
            "WHERE r.rentalId.customer.customerId = :customerId ")
    List<FilmRentResponse> getAllFilmRentResponses(long customerId);

    @Query(value = "SELECT DISTINCT new it.cgmconsulting.raffaele.payload.response.FilmMaxRentResponse(" +
            "r1.rentalId.inventory.film.filmId, " +
            "r1.rentalId.inventory.film.title, " +
            "(SELECT COUNT (r2.rentalId.inventory.film.filmId) FROM Rental r2 WHERE r2.rentalId.inventory.film.filmId = r1.rentalId.inventory.film.filmId ) AS total" +
            ") FROM Rental r1 " +
            "ORDER BY total DESC, r1.rentalId.inventory.film.title "
            )
    List<FilmMaxRentResponse> findFilmByNumberOfRent();

    @Query(value = "SELECT r " +
            "FROM Rental r " +
            "WHERE r.rentalId.customer = :customer " +
            "AND r.rentalId.inventory.film = :film " +
            "AND r.rentalId.inventory.store = :store " +
            "AND r.rentalReturn = null AND r.rentalId.rentalDate = :rentalDate " )
    Optional<Rental> findRentalsForUpdateWithRentalDate(Customer customer, Film film, Store store, LocalDateTime rentalDate);

}
