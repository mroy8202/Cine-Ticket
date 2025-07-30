package com.mritunjay.cineticket.dto.theatre;

import com.mritunjay.cineticket.dto.show.ShowSummaryResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TheatreResponseDTO {
    private Long theatreId;
    private String theatreName;
    private String theatreLocation;
    private Integer totalScreens;
    private List<ShowSummaryResponseDTO> shows;
}
