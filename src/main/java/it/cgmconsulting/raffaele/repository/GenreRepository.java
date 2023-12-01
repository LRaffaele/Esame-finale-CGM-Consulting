package it.cgmconsulting.raffaele.repository;

import it.cgmconsulting.raffaele.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByGenreName(String genreName);

}
