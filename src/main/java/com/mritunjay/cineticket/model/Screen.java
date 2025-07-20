package com.mritunjay.cineticket.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long screenId;

    private String screenName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "theatre_id")
    @JsonBackReference
    private Theatre theatre;

    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Seat> seats;

}
