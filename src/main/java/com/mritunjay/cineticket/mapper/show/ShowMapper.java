package com.mritunjay.cineticket.mapper.show;

import com.mritunjay.cineticket.dto.show.ShowDetailedResponseDTO;
import com.mritunjay.cineticket.dto.show.ShowResponseDTO;
import com.mritunjay.cineticket.dto.show.ShowSummaryResponseDTO;
import com.mritunjay.cineticket.mapper.movie.MovieMapper;
import com.mritunjay.cineticket.mapper.screen.ScreenMapper;
import com.mritunjay.cineticket.mapper.theatre.TheatreMapper;
import com.mritunjay.cineticket.model.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShowMapper {

    private final MovieMapper movieMapper;
    private final TheatreMapper theatreMapper;
    private final ScreenMapper screenMapper;

    @Autowired
    public ShowMapper(MovieMapper movieMapper, TheatreMapper theatreMapper, ScreenMapper screenMapper) {
        this.movieMapper = movieMapper;
        this.theatreMapper = theatreMapper;
        this.screenMapper = screenMapper;
    }

    // Show Entity -> Show Summary Response Dto
    public ShowSummaryResponseDTO convertShowEntityToShowSummaryResponseDto(Show show) {
        return ShowSummaryResponseDTO.builder()
                .showId(show.getShowId())
                .movie(movieMapper.convertMovieEntityToMovieSummaryResponseDto(show.getMovie()))
                .build();
    }

    // Show Entity -> Show Response Dto
    public ShowResponseDTO convertShowEntityToShowResponseDto(Show show) {
        return ShowResponseDTO.builder()
                .showId(show.getShowId())
                .movie(movieMapper.convertMovieEntityToMovieSummaryResponseDto(show.getMovie()))
                .theatre(theatreMapper.convertTheatreEntityToTheatreSummaryResponseDto(show.getTheatre()))
                .startTime(show.getStartTime())
                .endTime(show.getEndTime())
                .build();
    }

    // Show Entity -> Show Detailed Response Dto
    public ShowDetailedResponseDTO convertShowEntityToShowDetailedResponseDto(Show show) {
        return ShowDetailedResponseDTO.builder()
                .showId(show.getShowId())
                .movie(movieMapper.convertMovieEntityToMovieSummaryResponseDto(show.getMovie()))
                .theatre(theatreMapper.convertTheatreEntityToTheatreSummaryResponseDto(show.getTheatre()))
                .screen(screenMapper.convertScreenEntityToScreenSummaryResponseDto(show.getScreen()))
                .startTime(show.getStartTime())
                .endTime(show.getEndTime())
                .build();
    }

    // Show Detailed Response Dto -> Show Entity
    public Show convertShowDetailedResponseDtoToShowEntity(ShowDetailedResponseDTO showDetailedResponseDTO) {
        return Show.builder()
                .showId(showDetailedResponseDTO.getShowId())
                .movie(movieMapper.convertMovieSummaryResponseDtoToMovieEntity(showDetailedResponseDTO.getMovie()))
                .theatre(theatreMapper.convertTheatreSummaryResponseDtoToTheatreEntity(showDetailedResponseDTO.getTheatre()))
                .screen(screenMapper.convertScreenSummaryResponseDtoToScreenEntity(showDetailedResponseDTO.getScreen()))
                .startTime(showDetailedResponseDTO.getStartTime())
                .endTime(showDetailedResponseDTO.getEndTime())
                .build();
    }

    // Show Summary Response Dto -> Show Entity
    public Show convertShowSummaryResponseDtoToShowEntity(ShowSummaryResponseDTO showSummaryResponseDTO) {
        return Show.builder()
                .showId(showSummaryResponseDTO.getShowId())
                .movie(movieMapper.convertMovieSummaryResponseDtoToMovieEntity(showSummaryResponseDTO.getMovie()))
                .build();
    }
}
