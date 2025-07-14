package com.mritunjay.cineticket.service;

import com.mritunjay.cineticket.constants.ExceptionConstants;
import com.mritunjay.cineticket.dto.show.ShowRequestDTO;
import com.mritunjay.cineticket.exception.ShowNotFoundException;
import com.mritunjay.cineticket.model.*;
import com.mritunjay.cineticket.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final MovieService movieService;
    private final ScreenService screenService;
    public final ShowSeatService showSeatService;

    @Autowired
    ShowService(ShowRepository showRepository, MovieService movieService, ScreenService screenService, ShowSeatService showSeatService) {
        this.showRepository = showRepository;
        this.movieService = movieService;
        this.screenService = screenService;
        this.showSeatService = showSeatService;
    }

    public Page<Show> getAllShows(int page, int pageSize) {
        return showRepository.findAll(PageRequest.of(page, pageSize));
    }

    public Show getShowById(Long showId) {
        return showRepository
                .findById(showId)
                .orElseThrow(() -> new ShowNotFoundException(ExceptionConstants.SHOW_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public Page<Show> getShowsByMovieId(Long movieId, int page, int pageSize) {
            return showRepository
                    .findByMovie_MovieId(movieId, PageRequest.of(page, pageSize));
    }

    public Page<Show> getShowsByTheatreId(Long theatreId, int page, int pageSize) {
        return showRepository
                .findByTheatre_TheatreId(theatreId, PageRequest.of(page, pageSize));
    }

    public Page<Show> getShowsByScreenId(Long screenId, int page, int pageSize) {
        return showRepository
                .findByScreen_ScreenId(screenId,PageRequest.of(page, pageSize));
    }

    public Show createNewShow(ShowRequestDTO showRequestDTO) {
        Movie movie = movieService.getMovieById(showRequestDTO.getMovieId());

        Screen screen = screenService.getScreenById(showRequestDTO.getScreenId());

        List<ShowSeat> showSeats = new ArrayList<>();

        Show newShow = Show
                .builder()
                .movie(movie)
                .theatre(screen.getTheatre())
                .screen(screen)
                .startTime(showRequestDTO.getStartTime())
                .endTime(showRequestDTO.getEndTime())
                .build();

        for (Seat seat : screen.getSeats()) {
            ShowSeat showSeat = showSeatService.createNewShowSeat(newShow, seat);
            showSeats.add(showSeat);
        }

        newShow.setShowSeats(showSeats);

        return showRepository.save(newShow);
    }

    public Show updateShowById(Long showId, ShowRequestDTO showRequestDTO) {
        Movie movie = movieService.getMovieById(showRequestDTO.getMovieId());

        return showRepository
                .findById(showId)
                .map(show -> {
                    show.setMovie(movie);
                    show.setStartTime(showRequestDTO.getStartTime());
                    show.setEndTime(showRequestDTO.getEndTime());

                    return showRepository.save(show);
                })
                .orElseThrow(() -> new ShowNotFoundException(ExceptionConstants.SHOW_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public void deleteShowById(Long showId) {
        showRepository.deleteById(showId);
    }

}
