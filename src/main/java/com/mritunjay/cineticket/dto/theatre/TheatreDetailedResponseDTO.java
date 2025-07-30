package com.mritunjay.cineticket.dto.theatre;

import com.mritunjay.cineticket.dto.screen.ScreenResponseDTO;
import com.mritunjay.cineticket.dto.user.UserSummaryResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TheatreDetailedResponseDTO {
    private Long theatreId;
    private String theatreName;
    private String theatreLocation;
    private Integer totalScreens;
    private List<UserSummaryResponseDTO> theatreAdmins;
    private List<ScreenResponseDTO> screens;
}
