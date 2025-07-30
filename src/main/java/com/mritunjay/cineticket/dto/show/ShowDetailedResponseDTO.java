package com.mritunjay.cineticket.dto.show;

import com.mritunjay.cineticket.dto.movie.MovieSummaryResponseDTO;
import com.mritunjay.cineticket.dto.screen.ScreenSummaryResponseDTO;
import com.mritunjay.cineticket.dto.theatre.TheatreSummaryResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ShowDetailedResponseDTO {
    private Long showId;
    private MovieSummaryResponseDTO movie;
    private TheatreSummaryResponseDTO theatre;
    private ScreenSummaryResponseDTO screen;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
