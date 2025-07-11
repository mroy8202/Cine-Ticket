package com.mritunjay.cineticket.dto.movie;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MovieRequestDTO {
    private String movieName;
    private String movieGenre;
    private String movieDirector;
    private LocalDate movieReleaseDate;
    private String movieDescription;
    private Long movieDuration;
}
