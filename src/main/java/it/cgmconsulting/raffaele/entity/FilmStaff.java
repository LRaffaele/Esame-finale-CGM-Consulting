package it.cgmconsulting.raffaele.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode
public class FilmStaff {

    @EmbeddedId
    private FilmStaffId filmStaffId;

}
