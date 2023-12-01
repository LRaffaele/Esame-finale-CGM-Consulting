package it.cgmconsulting.raffaele.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Genre {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long genreId;

    @Column(length = 30, nullable = false, unique = true)
    private String genreName;

    public Genre(String genreName) {
        this.genreName = genreName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return genreId == genre.genreId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId);
    }
}
