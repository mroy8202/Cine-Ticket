package com.mritunjay.cineticket.controller;

import com.mritunjay.cineticket.dto.APIResponseDTO;
import com.mritunjay.cineticket.dto.PagedAPIResponseDTO;
import com.mritunjay.cineticket.dto.auth.AuthReponseDTO;
import com.mritunjay.cineticket.dto.movie.MovieRequestDTO;
import com.mritunjay.cineticket.model.Movie;
import com.mritunjay.cineticket.service.MovieService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/all")
    public ResponseEntity<PagedAPIResponseDTO> getAllMovies(
            @RequestParam int page,
            @RequestParam int pageSize
    ) {
        Page<Movie> movies = movieService.getAllMovies(page, pageSize);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PagedAPIResponseDTO
                        .builder()
                        .pageData(movies.getContent())
                        .totalElements(movies.getTotalElements())
                        .totalPages(movies.getTotalPages())
                        .currentLimit(movies.getNumberOfElements())
                        .build()
                );
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<APIResponseDTO> getMoviesById(
            @PathVariable Long movieId
    ) {
        Movie movie = movieService.getMovieById(movieId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(APIResponseDTO
                        .builder()
                        .data(movie).
                        build()
                );
    }

    @Secured("ROLE_SUPER_ADMIN")
    @PostMapping("/movie/create")
    public ResponseEntity<APIResponseDTO> createNewMovie(
            @RequestBody MovieRequestDTO movieRequestDTO
            ) {
        Movie newMovie = movieService.createNewMovie(movieRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(APIResponseDTO.builder()
                        .message("New Movie created with id: " + newMovie.getMovieId() + " and name: " + newMovie.getMovieName())
                        .data(newMovie)
                        .build()
                );
    }

    @Secured("ROLE_SUPER_ADMIN")
    @PutMapping("/movie/{movieId}")
    public ResponseEntity<APIResponseDTO> updateMovieById(
                @PathVariable Long movieId,
                @RequestBody MovieRequestDTO movieRequestDTO
    ) {
        Movie updatedMovie = movieService.updateMovieById(movieId, movieRequestDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIResponseDTO
                        .builder()
                        .message("Updated the movie with id: " + updatedMovie.getMovieId() + " and name: " + updatedMovie.getMovieName())
                        .data(updatedMovie)
                        .build()
                );
    }

    @Secured("ROLE_SUPER_ADMIN")
    @DeleteMapping("movie/{movieId}")
    public ResponseEntity<APIResponseDTO> deleteMovieById(
            @PathVariable Long movieId
    ) {
        Movie deletedMovie = movieService.getMovieById(movieId);
        movieService.deleteMovieById(movieId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIResponseDTO
                        .builder()
                        .message("Deleted the movie with id: " + deletedMovie.getMovieId() + " and name: " + deletedMovie.getMovieName())
                        .build()
                );
    }

}