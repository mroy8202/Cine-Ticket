package com.mritunjay.cineticket.service;

import com.mritunjay.cineticket.dto.movie.MovieRequestDTO;
import com.mritunjay.cineticket.dto.movie.MovieResponseDTO;
import com.mritunjay.cineticket.dto.movie.MovieSummaryResponseDTO;
import org.springframework.data.domain.Page;

public interface MovieService {

    // Get All Movies
    Page<MovieSummaryResponseDTO> getAllMovies(int page, int pageSize);

    // Get Movie By id
    MovieResponseDTO getMovieById(Long movieId);

    // Create New Movie
    MovieResponseDTO createNewMovie(MovieRequestDTO movieRequestDTO);

    // Update Movie
    MovieResponseDTO updateMovieById(Long movieId, MovieRequestDTO movieRequestDTO);

    // Delete Movie By id
    void deleteMovieById(Long movieId);

}
