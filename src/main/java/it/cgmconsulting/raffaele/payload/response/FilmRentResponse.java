package it.cgmconsulting.raffaele.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FilmRentResponse {

    // film_id, title, store_name

    private long filmId;
    private String title;
    private String storeName;
}
