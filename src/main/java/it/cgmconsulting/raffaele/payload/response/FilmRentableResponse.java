package it.cgmconsulting.raffaele.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FilmRentableResponse {

    // title, store_name, numero totale di copie in possesso del negozio, numero di copie disponibili

    private String title;
    private String storeName;
    private long totalCopies;
    private long availableCopies;
}
