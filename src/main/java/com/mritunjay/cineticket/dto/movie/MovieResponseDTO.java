package com.mritunjay.cineticket.dto.movie;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MovieResponseDTO {
    private Long movieId;
    private String movieName;
    private String movieGenre; // enum -> String
    private String movieDescription;
    private String movieDirector;
    private LocalDate movieReleaseDate;
    private Long movieDuration;
}
