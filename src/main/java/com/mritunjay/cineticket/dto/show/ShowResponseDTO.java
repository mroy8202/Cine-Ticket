package com.mritunjay.cineticket.dto.show;

import com.mritunjay.cineticket.dto.movie.MovieSummaryResponseDTO;
import com.mritunjay.cineticket.dto.theatre.TheatreSummaryResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ShowResponseDTO {
    private Long showId;
    private MovieSummaryResponseDTO movie;
    private TheatreSummaryResponseDTO theatre;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
