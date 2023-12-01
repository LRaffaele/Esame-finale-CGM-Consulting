package it.cgmconsulting.raffaele.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class Rental {

    @EmbeddedId
    private RentalId rentalId;

    private LocalDateTime rentalReturn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rental rental = (Rental) o;
        return Objects.equals(rentalId, rental.rentalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rentalId);
    }
}
