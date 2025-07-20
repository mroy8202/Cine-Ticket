package com.mritunjay.cineticket.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

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
    @JsonManagedReference
    private List<TheatreVsAdmin> theatreAdmins;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL)
    List<Show> shows;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    List<Screen> screens;

}
