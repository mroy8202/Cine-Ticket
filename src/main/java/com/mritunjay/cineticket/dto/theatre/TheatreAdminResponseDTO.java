package com.mritunjay.cineticket.dto.theatre;

import com.mritunjay.cineticket.dto.user.UserSummaryResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TheatreAdminResponseDTO {
    private Long theatreId;
    private String theatreName;
    private List<UserSummaryResponseDTO> theatreAdmins;
}
