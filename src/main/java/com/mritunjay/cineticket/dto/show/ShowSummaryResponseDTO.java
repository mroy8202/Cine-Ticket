package com.mritunjay.cineticket.dto.show;

import com.mritunjay.cineticket.dto.movie.MovieSummaryResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowSummaryResponseDTO {
    private Long showId;
    private MovieSummaryResponseDTO movie;
}
