package it.cgmconsulting.raffaele.repository;

import it.cgmconsulting.raffaele.entity.Film;
import it.cgmconsulting.raffaele.payload.response.FilmResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query(value = "SELECT new it.cgmconsulting.raffaele.payload.response.FilmResponse(" +
            "film.filmId, " +
            "film.title, " +
            "film.description, " +
            "film.releaseYear, " +
            "film.language.languageName" +
            ") FROM Film film " +
            "WHERE film.language.languageId = :languageId")
    List<FilmResponse> getFilmsByLanguageId(long languageId);

    boolean existsByTitle(String title);

}
