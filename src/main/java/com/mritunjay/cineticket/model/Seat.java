package com.mritunjay.cineticket.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mritunjay.cineticket.enums.SeatType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "row_seat_id", columnNames = {"screen_id", "rowId", "seatNumber"})
})
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    private Integer rowId;

    private Integer seatNumber;

    @Enumerated(value = EnumType.STRING)
    private SeatType seatType;

    @ManyToOne
    @JoinColumn(name = "screen_id")
    @JsonBackReference
    private Screen screen;

    private Double seatPrice;

}
