package com.mritunjay.cineticket.dto.seat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatResponseDTO {
    private Long seatId;
    private Integer rowNumber;
    private Integer seatNumber;
    private String seatType; // enum -> String
    private Double seatPrice;
}
