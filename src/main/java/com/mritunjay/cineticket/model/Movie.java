package com.mritunjay.cineticket.model;

import com.mritunjay.cineticket.enums.Genre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    private String movieName;

    @Enumerated(value = EnumType.STRING)
    private Genre movieGenre;

    private String movieDescription;

    private String movieDirector;

    private LocalDate movieReleaseDate;

    private Long movieDuration;

    private Integer totalBookings;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    List<Show> shows;

}
