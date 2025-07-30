package com.mritunjay.cineticket.dto.reservation;

import com.mritunjay.cineticket.dto.show.ShowDetailedResponseDTO;
import com.mritunjay.cineticket.dto.showseat.ShowSeatResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReservationResponseDTO {
    private Long reservationId;
    private ShowDetailedResponseDTO show;
    private List<ShowSeatResponseDTO> seatsReserved;
    private LocalDateTime reservationTime;
    private Double totalAmount;
    private String reservationStatus; // enum -> String
}
