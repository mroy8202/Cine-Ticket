package com.mritunjay.cineticket.dto.theatre;

import com.mritunjay.cineticket.dto.screen.ScreenRequestDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TheatreRequestDTO {
    private String theatreName;
    private String theatreLocation;
    private Long theatreAdminId;
    List<ScreenRequestDTO> screens;
}
