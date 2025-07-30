package com.mritunjay.cineticket.mapper.screen;

import com.mritunjay.cineticket.dto.screen.ScreenResponseDTO;
import com.mritunjay.cineticket.dto.screen.ScreenSummaryResponseDTO;
import com.mritunjay.cineticket.dto.seat.SeatResponseDTO;
import com.mritunjay.cineticket.mapper.seat.SeatMapper;
import com.mritunjay.cineticket.model.Screen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScreenMapper {

    private final SeatMapper seatMapper;

    @Autowired
    public ScreenMapper(SeatMapper seatMapper) {
        this.seatMapper = seatMapper;
    }

    // Screen Entity -> Screen Response Dto
    public ScreenResponseDTO convertScreenEntityToScreenResponseDto(Screen screen) {
        // Get List of SeatResponseDto's
        List<SeatResponseDTO> seatDtos = screen.getSeats().stream()
                .map(seatMapper::convertSeatEntityToSeatResponseDto)
                .toList();

        return ScreenResponseDTO.builder()
                .screenId(screen.getScreenId())
                .screenName(screen.getScreenName())
                .seats(seatDtos)
                .build();
    }

    // Screen Entity -> Screen Summary Response Dto
    public ScreenSummaryResponseDTO convertScreenEntityToScreenSummaryResponseDto(Screen screen) {
        return ScreenSummaryResponseDTO.builder()
                .screenId(screen.getScreenId())
                .screenName(screen.getScreenName())
                .build();
    }

    // Screen Summary Response Dto -> Screen Entity
    public Screen convertScreenSummaryResponseDtoToScreenEntity(ScreenSummaryResponseDTO screenSummaryResponseDTO) {
        return Screen.builder()
                .screenId(screenSummaryResponseDTO.getScreenId())
                .screenName(screenSummaryResponseDTO.getScreenName())
                .build();
    }

}
