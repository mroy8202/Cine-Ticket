package com.mritunjay.cineticket.model;

import com.mritunjay.cineticket.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "show_id")
    private Show show;

    @ManyToOne(fetch = FetchType.EAGER)
    List<ShowSeat> seatsReserved;

    private LocalDateTime reservationTime;

    private LocalDateTime updatedTime;

    private Double totalAmount;

    @Enumerated(value = EnumType.STRING)
    private ReservationStatus reservationStatus;

}
