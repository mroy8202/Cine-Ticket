package com.mritunjay.cineticket.mapper.movie;

import com.mritunjay.cineticket.dto.movie.MovieRequestDTO;
import com.mritunjay.cineticket.dto.movie.MovieResponseDTO;
import com.mritunjay.cineticket.dto.movie.MovieSummaryResponseDTO;
import com.mritunjay.cineticket.enums.Genre;
import com.mritunjay.cineticket.model.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    // Movie Request DTO -> Movie Entity
    public Movie convertMovieRequestDtoToMovieEntity(MovieRequestDTO movieRequestDTO) {
        return Movie.builder()
                .movieName(movieRequestDTO.getMovieName())
                .movieGenre(Genre.valueOf(movieRequestDTO.getMovieGenre()))
                .movieDirector(movieRequestDTO.getMovieDirector())
                .movieReleaseDate(movieRequestDTO.getMovieReleaseDate())
                .movieDescription(movieRequestDTO.getMovieDescription())
                .movieDuration(movieRequestDTO.getMovieDuration())
                .build();
    }

    // Movie Entity -> Movie Response Dto
    public MovieResponseDTO convertMovieEntityToMovieResponseDto(Movie movie) {
        return MovieResponseDTO.builder()
                .movieId(movie.getMovieId())
                .movieName(movie.getMovieName())
                .movieGenre(movie.getMovieGenre().toString())
                .movieDescription(movie.getMovieDescription())
                .movieDirector(movie.getMovieDirector())
                .movieReleaseDate(movie.getMovieReleaseDate())
                .movieDuration(movie.getMovieDuration())
                .build();
    }

    // Movie Entity -> Movie Summary Response Dto
    public MovieSummaryResponseDTO convertMovieEntityToMovieSummaryResponseDto(Movie movie) {
        return MovieSummaryResponseDTO.builder()
                .movieId(movie.getMovieId())
                .movieName(movie.getMovieName())
                .movieGenre(movie.getMovieGenre().toString())
                .build();
    }

    // Movie Summary Response Dto -> Movie Entity
    public Movie convertMovieSummaryResponseDtoToMovieEntity(MovieSummaryResponseDTO movieSummaryResponseDTO) {
        return Movie.builder()
                .movieId(movieSummaryResponseDTO.getMovieId())
                .movieName(movieSummaryResponseDTO.getMovieName())
                .movieGenre(Genre.valueOf(movieSummaryResponseDTO.getMovieGenre()))
                .build();
    }

}
