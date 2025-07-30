package com.mritunjay.cineticket.dto.seat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatRequestDTO {
    private Integer rowNumber;
    private Integer seatNumber;
    private String seatType;
    private Double seatPrice;
}
