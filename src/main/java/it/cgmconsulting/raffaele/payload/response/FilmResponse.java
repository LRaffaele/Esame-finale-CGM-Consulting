package it.cgmconsulting.raffaele.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FilmResponse {

    // film_id, title, description, release_year, language_name

    private long filmId;
    private String title;
    private String description;
    private short releaseYear;
    private String languageName;

}
