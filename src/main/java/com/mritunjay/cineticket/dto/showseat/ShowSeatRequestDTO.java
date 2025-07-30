package com.mritunjay.cineticket.dto.showseat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowSeatRequestDTO {
    private Long seatId;
}
