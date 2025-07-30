package com.mritunjay.cineticket.service.impl;

import com.mritunjay.cineticket.dto.seat.SeatRequestDTO;
import com.mritunjay.cineticket.enums.SeatType;
import com.mritunjay.cineticket.model.Screen;
import com.mritunjay.cineticket.model.Seat;
import com.mritunjay.cineticket.service.SeatService;
import org.springframework.stereotype.Service;

@Service
public class SeatServiceImpl implements SeatService {

    @Override
    public Seat createNewSeat(Screen screen, SeatRequestDTO seatRequestDTO) {
        return Seat
                .builder()
                .rowNumber(seatRequestDTO.getRowNumber())
                .seatNumber(seatRequestDTO.getSeatNumber())
                .seatPrice(seatRequestDTO.getSeatPrice())
                .seatType(SeatType.valueOf(seatRequestDTO.getSeatType()))
                .screen(screen) // parent reference
                .build();
    }

}
