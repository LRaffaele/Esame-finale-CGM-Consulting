package it.cgmconsulting.raffaele.repository;

import it.cgmconsulting.raffaele.entity.FilmStaff;
import it.cgmconsulting.raffaele.entity.FilmStaffId;
import it.cgmconsulting.raffaele.payload.response.FilmResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface FilmStaffRepository extends JpaRepository<FilmStaff, FilmStaffId> {

    @Query(value = "SELECT s.filmStaffId.staff.staffId " +
            "FROM FilmStaff s " +
            "WHERE s.filmStaffId.role.roleName = :role " +
            "AND s.filmStaffId.staff.staffId IN :staffIds ")
    Set<Long> filterStaffIdsByRole(String role, Set<Long> staffIds);

    @Query(value = "SELECT new it.cgmconsulting.raffaele.payload.response.FilmResponse(" +
            "f.filmStaffId.film.filmId, " +
            "f.filmStaffId.film.title, " +
            "f.filmStaffId.film.description, " +
            "f.filmStaffId.film.releaseYear, " +
            "f.filmStaffId.film.language.languageName " +
            ") FROM FilmStaff f " +
            "WHERE (f.filmStaffId.staff.staffId) IN :actors " +
            "GROUP BY f.filmStaffId.film.filmId " +
            "HAVING COUNT(f.filmStaffId.staff.staffId) = :size " +
            "ORDER BY f.filmStaffId.film.title" )
    List<FilmResponse> findByCast(Set<Long> actors, long size);

}
