package com.mritunjay.cineticket.service.impl;

import com.mritunjay.cineticket.constants.ExceptionConstants;
import com.mritunjay.cineticket.dto.screen.ScreenRequestDTO;
import com.mritunjay.cineticket.exception.ScreenNotFoundException;
import com.mritunjay.cineticket.model.Screen;
import com.mritunjay.cineticket.model.Seat;
import com.mritunjay.cineticket.model.Theatre;
import com.mritunjay.cineticket.repository.ScreenRepository;
import com.mritunjay.cineticket.service.ScreenService;
import com.mritunjay.cineticket.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository screenRepository;
    private final SeatService seatService;

    @Autowired
    ScreenServiceImpl(ScreenRepository screenRepository, SeatService seatService) {
        this.screenRepository = screenRepository;
        this.seatService = seatService;
    }

    @Override
    public Screen getScreenById(Long screenId) {
        return screenRepository
                .findById(screenId)
                .orElseThrow(() -> new ScreenNotFoundException(ExceptionConstants.SCREEN_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public Screen createNewScreen(Theatre theatre, ScreenRequestDTO screenRequestDTO) {
        Screen screen = Screen.builder()
                .screenName(screenRequestDTO.getScreenName())
                .theatre(theatre) // parent reference
                .build();

        List<Seat> seats = screenRequestDTO.getSeats().stream()
                .map(seatDto -> seatService.createNewSeat(screen, seatDto))
                .toList();

        screen.setSeats(seats);

        return screen;
    }

}
