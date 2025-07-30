package com.mritunjay.cineticket.dto.screen;

import com.mritunjay.cineticket.dto.seat.SeatResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScreenResponseDTO {
    private Long screenId;
    private String screenName;
    private List<SeatResponseDTO> seats;
}
