package com.mritunjay.cineticket.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TheatreAdminRequestDTO {
    Long userId;
    Long theatreId;
}
