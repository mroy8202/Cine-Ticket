package com.mritunjay.cineticket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theatreId;

    private String theatreName;

    private String theatreLocation;

    private Integer totalScreens;

    private Integer totalBookings;

    private Double totalRevenue;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL)
    private List<TheatreAdmin> theatreAdmins;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL)
    List<Show> shows;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Screen> screens;

}
