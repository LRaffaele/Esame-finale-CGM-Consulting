package it.cgmconsulting.raffaele.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RentalAddOrUpdateRequest {

    @Min(1)
    private long customerId;
    @Min(1)
    private long filmId;
    @Min(1)
    private long storeId;

    @PastOrPresent
    private LocalDateTime rentalReturn;

    @PastOrPresent
    private LocalDateTime rentalDate;

}
