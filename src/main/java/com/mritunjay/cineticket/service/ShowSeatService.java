package com.mritunjay.cineticket.service;

import com.mritunjay.cineticket.model.Seat;
import com.mritunjay.cineticket.model.Show;
import com.mritunjay.cineticket.model.ShowSeat;

import java.util.List;

public interface ShowSeatService {

    // Create New Show Seat
    ShowSeat createNewShowSeat(Show show, Seat seat);

    // Get Show Seats
    List<ShowSeat> getShowSeats(List<Long> showSeatsIds);

    // Get Available Seats
    List<ShowSeat> getAvailableSeats(List<Long> showSeatsIds);

    // Book Seats And Return Total Amount
    Double bookSeatsAndReturnTotalAmount(List<ShowSeat> showSeats);

    // Acquire Locks For Show Seats
    void acquireLocksForShowSeats(List<Long> showSeatIds);

    // Remove Locks For Show Seats
    void removeLocksForShowSeats(List<Long> showSeatIds);
}
