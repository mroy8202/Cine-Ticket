package com.mritunjay.cineticket.service;

import com.mritunjay.cineticket.dto.movie.MovieRequestDTO;
import com.mritunjay.cineticket.enums.Genre;
import com.mritunjay.cineticket.model.Movie;
import com.mritunjay.cineticket.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Page<Movie> getAllMovies(int page, int pageSize) {
        return movieRepository.findAll(PageRequest.of(page, pageSize));
    }

    public Movie getMovieById(Long movieId) {
        return movieRepository.findById(movieId)
                //.orElseThrow(MovieNotzFoundException);
    }

    public Movie createNewMovie(MovieRequestDTO movieRequestDTO) {
        Movie movie = Movie
                .builder()
                .movieName(movieRequestDTO.getMovieName())
                .movieDescription(movieRequestDTO.getMovieDescription())
                .movieDirector(movieRequestDTO.getMovieDirector())
                .movieGenre(Genre.valueOf(movieRequestDTO.getMovieGenre()))
                .movieReleaseDate(movieRequestDTO.getMovieReleaseDate())
                .movieDuration(movieRequestDTO.getMovieDuration())
                .totalBookings(0)
                .build();

        return movieRepository.save(movie);
    }

    public Movie updateMovieById(Long movieId, MovieRequestDTO movieRequestDTO) {
        return movieRepository
                .findById(movieId)
                .map(movie -> {
                    movie.setMovieName(movieRequestDTO.getMovieName());
                    movie.setMovieDescription(movieRequestDTO.getMovieDescription());
                    movie.setMovieDirector(movieRequestDTO.getMovieDirector());
                    movie.setMovieGenre(Genre.valueOf(movieRequestDTO.getMovieGenre()));
                    movie.setMovieReleaseDate(movieRequestDTO.getMovieReleaseDate());
                    movie.setMovieDuration(movieRequestDTO.getMovieDuration());
                    return movieRepository.save(movie);
                })
//                .orElseThrow(MovieNotFoundException)
    }

    public void deleteMovieById(Long movieId) {
        movieRepository.deleteById(movieId);
    }
}
