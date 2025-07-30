package com.mritunjay.cineticket.service.impl;

import com.mritunjay.cineticket.constants.ExceptionConstants;
import com.mritunjay.cineticket.dto.movie.MovieRequestDTO;
import com.mritunjay.cineticket.dto.movie.MovieResponseDTO;
import com.mritunjay.cineticket.dto.movie.MovieSummaryResponseDTO;
import com.mritunjay.cineticket.enums.Genre;
import com.mritunjay.cineticket.exception.MovieNotFoundException;
import com.mritunjay.cineticket.mapper.movie.MovieMapper;
import com.mritunjay.cineticket.model.Movie;
import com.mritunjay.cineticket.repository.MovieRepository;
import com.mritunjay.cineticket.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Autowired
    MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    @Override
    public Page<MovieSummaryResponseDTO> getAllMovies(int page, int pageSize) {
        Page<Movie> movies = movieRepository.findAll(PageRequest.of(page, pageSize));
        return movies.map(movieMapper::convertMovieEntityToMovieSummaryResponseDto);
    }

    @Override
    public MovieResponseDTO getMovieById(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(ExceptionConstants.MOVIE_NOT_FOUND, HttpStatus.NOT_FOUND));

        return movieMapper.convertMovieEntityToMovieResponseDto(movie);
    }

    @Override
    public MovieResponseDTO createNewMovie(MovieRequestDTO movieRequestDTO) {
        Movie movie = movieMapper.convertMovieRequestDtoToMovieEntity(movieRequestDTO);
        movie.setTotalBookings(0);

        Movie savedMovie = movieRepository.save(movie);

        return movieMapper.convertMovieEntityToMovieResponseDto(savedMovie);
    }

    @Override
    public MovieResponseDTO updateMovieById(Long movieId, MovieRequestDTO movieRequestDTO) {
        Movie movieToBeUpdated = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(ExceptionConstants.MOVIE_NOT_FOUND, HttpStatus.NOT_FOUND));

        movieToBeUpdated.setMovieName(movieRequestDTO.getMovieName());
        movieToBeUpdated.setMovieGenre(Genre.valueOf(movieRequestDTO.getMovieGenre()));
        movieToBeUpdated.setMovieDirector(movieRequestDTO.getMovieDirector());
        movieToBeUpdated.setMovieReleaseDate(movieRequestDTO.getMovieReleaseDate());
        movieToBeUpdated.setMovieDescription(movieRequestDTO.getMovieDescription());
        movieToBeUpdated.setMovieDuration(movieRequestDTO.getMovieDuration());

        Movie updatedMovie = movieRepository.save(movieToBeUpdated);

        return movieMapper.convertMovieEntityToMovieResponseDto(updatedMovie);
    }

    @Override
    public void deleteMovieById(Long movieId) {
        movieRepository.deleteById(movieId);
    }
}
