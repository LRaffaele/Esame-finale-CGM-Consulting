package it.cgmconsulting.raffaele.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class FilmMaxRentResponse {

    // film_id, title, numero di noleggi totale.

    private long filmId;
    private String title;
    private long totalRents;
}
