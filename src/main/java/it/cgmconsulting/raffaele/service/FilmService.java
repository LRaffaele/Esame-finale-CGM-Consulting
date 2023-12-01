package it.cgmconsulting.raffaele.service;

import it.cgmconsulting.raffaele.entity.Film;
import it.cgmconsulting.raffaele.entity.Genre;
import it.cgmconsulting.raffaele.entity.Language;
import it.cgmconsulting.raffaele.exception.ResourceNotFoundException;
import it.cgmconsulting.raffaele.payload.request.FilmRequest;
import it.cgmconsulting.raffaele.payload.response.FilmResponse;
import it.cgmconsulting.raffaele.repository.FilmRepository;
import it.cgmconsulting.raffaele.repository.FilmStaffRepository;
import it.cgmconsulting.raffaele.repository.GenreRepository;
import it.cgmconsulting.raffaele.repository.LanguageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final LanguageRepository languageRepository;
    private final GenreRepository genreRepository;
    private final FilmStaffRepository filmStaffRepository;

    @Transactional
    public ResponseEntity<?> updateFilm(long filmId, FilmRequest request) {

        // Controllo che ai dati della request corrispondano un film/language/genre presenti nel db
        Film film = getFilmEntityFromId(filmId);

        Language language = languageRepository.findByLanguageName(request.getLanguageName()).orElseThrow(
                () -> new ResourceNotFoundException("Language", "languageName", request.getLanguageName())
        );

        Genre genre = genreRepository.findByGenreName(request.getGenreName()).orElseThrow(
                () -> new ResourceNotFoundException("Genre", "genreName", request.getGenreName())
        );

        // aggiorno il record del film e persisto sul db con @Transactional
        film.setTitle(request.getTitle().trim().toUpperCase());
        film.setDescription(request.getDescription().trim());
        film.setReleaseYear(request.getReleaseYear());
        film.setLanguage(language);
        film.setGenre(genre);

        return new ResponseEntity<>("Film " + filmId + " successfully updated ", HttpStatus.OK);
    }

    public Film getFilmEntityFromId(long filmId){
        Film film = filmRepository.findById(filmId).orElseThrow(
                () -> new ResourceNotFoundException("Film", "filmId", filmId)
        );
        return film;
    }

    public ResponseEntity<?> findFilmsByLanguageId(long languageId) {

        // controllo che al languageId corrisponda una language sul db
        if(!languageRepository.existsById(languageId))
            return new ResponseEntity<>("Language not found", HttpStatus.NOT_FOUND);

        List<FilmResponse> filmResponseList = filmRepository.getFilmsByLanguageId(languageId);

        return new ResponseEntity<>(filmResponseList, HttpStatus.OK);
    }

    public ResponseEntity<?> findFilmsByCast(Set<Long> staffIds) {

        Set<Long> actors = filterStaffIdsByRole("ACTOR", staffIds); // ritorna una lista di ID filtrata per il ruolo selezionato

        if(actors.isEmpty())
            return new ResponseEntity<>("Could not find any actors matching the selected ids", HttpStatus.NOT_FOUND);

        List<FilmResponse> films = filmStaffRepository.findByCast(actors, actors.size()); // passo la actor.size() nella query come condizione per ottenere dalla lista i soli film in cui TUTTI gli attori hanno lavorato

        return new ResponseEntity(films, HttpStatus.OK);
    }

    public Set<Long> filterStaffIdsByRole(String roleName, Set<Long> staffIds){
        return filmStaffRepository.filterStaffIdsByRole(roleName, staffIds);
    }


}
