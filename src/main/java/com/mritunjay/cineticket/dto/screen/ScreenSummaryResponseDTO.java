package com.mritunjay.cineticket.dto.screen;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScreenSummaryResponseDTO {
    private Long screenId;
    private String screenName;
}
