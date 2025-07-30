package com.mritunjay.cineticket.mapper.reservation;

import com.mritunjay.cineticket.dto.reservation.ReservationResponseDTO;
import com.mritunjay.cineticket.dto.showseat.ShowSeatResponseDTO;
import com.mritunjay.cineticket.mapper.show.ShowMapper;
import com.mritunjay.cineticket.mapper.showseat.ShowSeatMapper;
import com.mritunjay.cineticket.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationMapper {

    private final ShowMapper showMapper;
    private final ShowSeatMapper showSeatMapper;

    @Autowired
    public ReservationMapper(ShowMapper showMapper, ShowSeatMapper showSeatMapper) {
        this.showMapper = showMapper;
        this.showSeatMapper = showSeatMapper;
    }

    // Reservation Entity -> Reservation Response Dto
    public ReservationResponseDTO convertReservationEntityToReservationResponseDto(Reservation reservation) {

        List<ShowSeatResponseDTO> showSeatDtos = reservation.getSeatsReserved().stream()
                .map(showSeatMapper::convertShowSeatEntityToShowSeatResponseDto)
                .toList();

        return ReservationResponseDTO.builder()
                .reservationId(reservation.getReservationId())
                .show(showMapper.convertShowEntityToShowDetailedResponseDto(reservation.getShow()))
                .seatsReserved(showSeatDtos)
                .reservationTime(reservation.getReservationTime())
                .totalAmount(reservation.getTotalAmount())
                .reservationStatus(reservation.getReservationStatus().toString())
                .build();
    }

}
