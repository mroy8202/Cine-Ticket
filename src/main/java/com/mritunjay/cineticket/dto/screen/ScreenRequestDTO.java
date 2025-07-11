package com.mritunjay.cineticket.dto.screen;

import com.mritunjay.cineticket.dto.seat.SeatRequestDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScreenRequestDTO {
    private String screenName;
    List<SeatRequestDTO> seats;
}
