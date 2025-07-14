package com.mritunjay.cineticket.service;

import com.mritunjay.cineticket.dto.seat.SeatRequestDTO;
import com.mritunjay.cineticket.enums.SeatType;
import com.mritunjay.cineticket.model.Screen;
import com.mritunjay.cineticket.model.Seat;
import com.mritunjay.cineticket.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    @Autowired
    SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Seat createNewSeat(Screen screen, SeatRequestDTO seatRequestDTO) {
        Seat seat = Seat
                .builder()
                .rowId(seatRequestDTO.getRowId())
                .seatNumber(seatRequestDTO.getSeatNumber())
                .seatPrice(seatRequestDTO.getSeatPrice())
                .seatType(SeatType.valueOf(seatRequestDTO.getSeatType()))
                .screen(screen)
                .build();

        return seatRepository.save(seat);
    }

}
