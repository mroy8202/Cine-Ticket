package com.mritunjay.cineticket.mapper.showseat;

import com.mritunjay.cineticket.dto.showseat.ShowSeatResponseDTO;
import com.mritunjay.cineticket.mapper.seat.SeatMapper;
import com.mritunjay.cineticket.model.ShowSeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShowSeatMapper {

    private final SeatMapper seatMapper;

    @Autowired
    public ShowSeatMapper(SeatMapper seatMapper) {
        this.seatMapper = seatMapper;
    }

    // ShowSeat Entity -> ShowSeat Response Dto
    public ShowSeatResponseDTO convertShowSeatEntityToShowSeatResponseDto(ShowSeat showSeat) {
        return ShowSeatResponseDTO.builder()
                .showSeatId(showSeat.getShowSeatId())
                .seatStatus(showSeat.getSeatStatus().toString())
                .seat(seatMapper.convertSeatEntityToSeatResponseDto(showSeat.getSeat()))
                .build();
    }

}
