package com.mritunjay.cineticket.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSummaryResponseDTO {
    private Long userId;
    private String username;
}
