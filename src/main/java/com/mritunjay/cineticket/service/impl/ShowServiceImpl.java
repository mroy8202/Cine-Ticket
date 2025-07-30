package com.mritunjay.cineticket.service.impl;

import com.mritunjay.cineticket.constants.ExceptionConstants;
import com.mritunjay.cineticket.dto.show.ShowDetailedResponseDTO;
import com.mritunjay.cineticket.dto.show.ShowRequestDTO;
import com.mritunjay.cineticket.dto.show.ShowResponseDTO;
import com.mritunjay.cineticket.exception.ShowNotFoundException;
import com.mritunjay.cineticket.mapper.movie.MovieMapper;
import com.mritunjay.cineticket.mapper.show.ShowMapper;
import com.mritunjay.cineticket.model.*;
import com.mritunjay.cineticket.repository.ShowRepository;
import com.mritunjay.cineticket.service.MovieService;
import com.mritunjay.cineticket.service.ScreenService;
import com.mritunjay.cineticket.service.ShowSeatService;
import com.mritunjay.cineticket.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final MovieService movieService;
    private final ScreenService screenService;
    public final ShowSeatService showSeatService;

    private final ShowMapper showMapper;
    private final MovieMapper movieMapper;

    @Autowired
    ShowServiceImpl(ShowRepository showRepository, MovieService movieService, ScreenService screenService, ShowSeatService showSeatService, ShowMapper showMapper, MovieMapper movieMapper) {
        this.showRepository = showRepository;
        this.movieService = movieService;
        this.screenService = screenService;
        this.showSeatService = showSeatService;
        this.showMapper = showMapper;
        this.movieMapper = movieMapper;
    }

    @Override
    public Page<ShowResponseDTO> getAllShows(int page, int pageSize) {
        Page<Show> allShows = showRepository.findAll(PageRequest.of(page, pageSize));
        return allShows.map(showMapper::convertShowEntityToShowResponseDto);
    }

    @Override
    public Page<ShowResponseDTO> getShowsByMovieId(Long movieId, int page, int pageSize) {
            Page<Show> showsByMovie = showRepository
                    .findByMovie_MovieId(movieId, PageRequest.of(page, pageSize));

            return showsByMovie.map(showMapper::convertShowEntityToShowResponseDto);
    }

    @Override
    public Page<ShowDetailedResponseDTO> getShowsByScreenId(Long screenId, int page, int pageSize) {
        Page<Show> showsByScreen = showRepository
                .findByScreen_ScreenId(screenId,PageRequest.of(page, pageSize));

        return showsByScreen.map(showMapper::convertShowEntityToShowDetailedResponseDto);
    }

    @Override
    public Page<ShowResponseDTO> getShowsByTheatreId(Long theatreId, int page, int pageSize) {
        Page<Show> showsByTheatre = showRepository
                .findByTheatre_TheatreId(theatreId, PageRequest.of(page, pageSize));

        return showsByTheatre.map(showMapper::convertShowEntityToShowResponseDto);
    }

    @Override
    public ShowDetailedResponseDTO getShowById(Long showId) {
        Show show = showRepository
                .findById(showId)
                .orElseThrow(() -> new ShowNotFoundException(ExceptionConstants.SHOW_NOT_FOUND, HttpStatus.NOT_FOUND));

        return showMapper.convertShowEntityToShowDetailedResponseDto(show);
    }

    @Override
    public ShowDetailedResponseDTO createNewShow(ShowRequestDTO showRequestDTO) {
        // Get the movie
        Movie movie = movieMapper.convertMovieResponseDtoToMovieEntity(
                movieService.getMovieById(showRequestDTO.getMovieId())
        );
        movie.setTotalBookings(0);

        // Get the screen
        Screen screen = screenService.getScreenById(showRequestDTO.getScreenId());

        // Partially build Show Entity
        Show newShow = Show
                .builder()
                .movie(movie)
                .theatre(screen.getTheatre())
                .screen(screen)
                .startTime(showRequestDTO.getStartTime())
                .endTime(showRequestDTO.getEndTime())
                .build();

        // Create new Show Seats
        List<ShowSeat> showSeats = screen.getSeats()
                .stream()
                .map(seat -> showSeatService.createNewShowSeat(newShow, seat))
                .toList();

        newShow.setShowSeats(showSeats);

        // Save Show to db
        Show savedShow = showRepository.save(newShow);
        return showMapper.convertShowEntityToShowDetailedResponseDto(savedShow);
    }

    @Override
    public ShowDetailedResponseDTO updateShowById(Long showId, ShowRequestDTO showRequestDTO) {
        // Get the movie
        Movie movie = movieMapper.convertMovieResponseDtoToMovieEntity(
                movieService.getMovieById(showRequestDTO.getMovieId())
        );

        Show updatedShow = showRepository
                .findById(showId)
                .map(show -> {
                    show.setMovie(movie);
                    show.setStartTime(showRequestDTO.getStartTime());
                    show.setEndTime(showRequestDTO.getEndTime());

                    return showRepository.save(show);
                })
                .orElseThrow(() -> new ShowNotFoundException(ExceptionConstants.SHOW_NOT_FOUND, HttpStatus.NOT_FOUND));

        return showMapper.convertShowEntityToShowDetailedResponseDto(updatedShow);
    }

    @Override
    public void deleteShowById(Long showId) {
        showRepository.deleteById(showId);
    }

}
