package com.mritunjay.cineticket.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_theatre_name_location",
                        columnNames = {"theatreName", "theatreLocation"}
                )
        }
)
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theatreId;

    private String theatreName;

    private String theatreLocation;

    private Integer totalScreens;

    private Integer totalBookings;

    private Double totalRevenue;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TheatreVsAdmin> theatreAdmins;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL)
    List<Show> shows;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Screen> screens;

}
