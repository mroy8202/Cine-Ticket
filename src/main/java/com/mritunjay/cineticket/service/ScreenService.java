package com.mritunjay.cineticket.service;

import com.mritunjay.cineticket.constants.ExceptionConstants;
import com.mritunjay.cineticket.dto.screen.ScreenRequestDTO;
import com.mritunjay.cineticket.dto.seat.SeatRequestDTO;
import com.mritunjay.cineticket.exception.ScreenNotFoundException;
import com.mritunjay.cineticket.model.Screen;
import com.mritunjay.cineticket.model.Seat;
import com.mritunjay.cineticket.model.Theatre;
import com.mritunjay.cineticket.repository.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final SeatService seatService;

    @Autowired
    ScreenService(ScreenRepository screenRepository, SeatService seatService) {
        this.screenRepository = screenRepository;
        this.seatService = seatService;
    }

    public Screen getScreenById(Long screenId) {
        return screenRepository
                .findById(screenId)
                .orElseThrow(() -> new ScreenNotFoundException(ExceptionConstants.SCREEN_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public Screen createNewScreen(Theatre theatre, ScreenRequestDTO screenRequestDTO) {
        Screen screen = Screen
                .builder()
                .screenName(screenRequestDTO.getScreenName())
                .theatre(theatre)
                .build();

        List<Seat> seats = new ArrayList<>();

        for (SeatRequestDTO seatRequestDTO: screenRequestDTO.getSeats()) {
            Seat seat = seatService.createNewSeat(screen, seatRequestDTO);
            seats.add(seat);
        }
        screen.setSeats(seats);

        return screenRepository.save(screen);
    }

}
