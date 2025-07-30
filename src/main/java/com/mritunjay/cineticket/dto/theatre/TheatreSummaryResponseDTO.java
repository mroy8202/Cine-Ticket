package com.mritunjay.cineticket.dto.theatre;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TheatreSummaryResponseDTO {
    private Long theatreId;
    private String theatreName;
    private String theatreLocation;
}
