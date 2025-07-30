package com.mritunjay.cineticket.service;

import com.mritunjay.cineticket.dto.seat.SeatRequestDTO;
import com.mritunjay.cineticket.model.Screen;
import com.mritunjay.cineticket.model.Seat;

public interface SeatService {

    // Create New Seat
    Seat createNewSeat(Screen screen, SeatRequestDTO seatRequestDTO);

}
