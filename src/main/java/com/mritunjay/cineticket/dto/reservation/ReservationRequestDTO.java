package com.mritunjay.cineticket.dto.reservation;

import com.mritunjay.cineticket.dto.showSeat.ShowSeatRequestDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReservationRequestDTO {
    private Long userId;
    private Long showId;
    private List<ShowSeatRequestDTO> showSeats;
}
