package it.cgmconsulting.raffaele.payload.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class FilmRequest {

    @NotBlank @Size(max=100)
    private String title;

    @NotBlank @Size(max = 65535)
    private String description;

    @Min(1890) @Max(2100)
    private short releaseYear;

    @NotBlank @Size(max=20)
    private String languageName;

    @NotBlank @Size(max=30)
    private String genreName;
}
