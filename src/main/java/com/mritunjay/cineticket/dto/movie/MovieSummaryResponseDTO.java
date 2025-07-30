package com.mritunjay.cineticket.dto.movie;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieSummaryResponseDTO {
    private Long movieId;
    private String movieName;
    private String movieGenre; // enum -> String
}
