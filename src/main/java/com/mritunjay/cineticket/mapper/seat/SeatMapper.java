package com.mritunjay.cineticket.mapper.seat;

import com.mritunjay.cineticket.dto.seat.SeatResponseDTO;
import com.mritunjay.cineticket.model.Seat;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

    // Convert Seat Entity to Seat Response Dto
    public SeatResponseDTO convertSeatEntityToSeatResponseDto(Seat seat) {
        return SeatResponseDTO.builder()
                .seatId(seat.getSeatId())
                .rowNumber(seat.getRowNumber())
                .seatNumber(seat.getSeatNumber())
                .seatType(seat.getSeatType().toString())
                .seatPrice(seat.getSeatPrice())
                .build();
    }

}
