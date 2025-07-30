package com.mritunjay.cineticket.dto.showseat;

import com.mritunjay.cineticket.dto.seat.SeatResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowSeatResponseDTO {
    private Long showSeatId;
    private String seatStatus; // enum -> String
    private SeatResponseDTO seat;
}
