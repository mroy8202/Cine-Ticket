package com.mritunjay.cineticket.mapper.theatre;

import com.mritunjay.cineticket.dto.screen.ScreenResponseDTO;
import com.mritunjay.cineticket.dto.show.ShowSummaryResponseDTO;
import com.mritunjay.cineticket.dto.theatre.TheatreAdminResponseDTO;
import com.mritunjay.cineticket.dto.theatre.TheatreDetailedResponseDTO;
import com.mritunjay.cineticket.dto.theatre.TheatreResponseDTO;
import com.mritunjay.cineticket.dto.theatre.TheatreSummaryResponseDTO;
import com.mritunjay.cineticket.dto.user.UserSummaryResponseDTO;
import com.mritunjay.cineticket.mapper.screen.ScreenMapper;
import com.mritunjay.cineticket.mapper.show.ShowMapper;
import com.mritunjay.cineticket.mapper.user.UserMapper;
import com.mritunjay.cineticket.model.Show;
import com.mritunjay.cineticket.model.Theatre;
import com.mritunjay.cineticket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TheatreMapper {

    private final ScreenMapper screenMapper;
    private final UserMapper userMapper;
    private final ShowMapper showMapper;

    @Autowired
    public TheatreMapper(ScreenMapper screenMapper, UserMapper userMapper, ShowMapper showMapper) {
        this.screenMapper = screenMapper;
        this.userMapper = userMapper;
        this.showMapper = showMapper;
    }

    // Theatre Entity -> Theatre Summary Response Dto
    public TheatreSummaryResponseDTO convertTheatreEntityToTheatreSummaryResponseDto(Theatre theatre) {
        return TheatreSummaryResponseDTO.builder()
                .theatreId(theatre.getTheatreId())
                .theatreName(theatre.getTheatreName())
                .theatreLocation(theatre.getTheatreLocation())
                .build();
    }

    // Theatre Entity -> Theatre Response Dto
    public TheatreResponseDTO convertTheatreEntityToTheatreResponseEntity(Theatre theatre) {

        List<ShowSummaryResponseDTO> showsDtos = theatre.getShows().stream()
                .map(showMapper::convertShowEntityToShowSummaryResponseDto)
                .toList();

        return TheatreResponseDTO.builder()
                .theatreId(theatre.getTheatreId())
                .theatreName(theatre.getTheatreName())
                .theatreLocation(theatre.getTheatreLocation())
                .totalScreens(theatre.getTotalScreens())
                .shows(showsDtos)
                .build();
    }

    // Theatre Entity -> Theatre Detailed Response Dto
    public TheatreDetailedResponseDTO convertTheatreEntityToTheatreDetailedResponseDto(Theatre theatre) {
        // Get all theatre admin users Dtos
        List<UserSummaryResponseDTO> adminDtos = theatre.getTheatreAdmins().stream()
                .map(theatreVsAdmin -> {
                    User user = theatreVsAdmin.getUser();
                    return userMapper.convertUserEntityToUserSummaryResponseDto(user);
                })
                .toList();

        // Get all Screen Dtos
        List<ScreenResponseDTO> screenDtos = theatre.getScreens().stream()
                .map(screen -> screenMapper.convertScreenEntityToScreenResponseDto(screen))
                .toList();

        return TheatreDetailedResponseDTO.builder()
                .theatreId(theatre.getTheatreId())
                .theatreName(theatre.getTheatreName())
                .theatreLocation(theatre.getTheatreLocation())
                .totalScreens(theatre.getTotalScreens())
                .theatreAdmins(adminDtos)
                .screens(screenDtos)
                .build();
    }

    // Theatre Entity -> Theatre Admin Response Dto
    public TheatreAdminResponseDTO convertTheatreEntityToTheatreAdminResponseDto(Theatre theatre) {
        // Get all Theatre Admin Dtos
        List<UserSummaryResponseDTO> adminDtos = theatre.getTheatreAdmins().stream()
                .map(theatreVsAdmin -> {
                    User user = theatreVsAdmin.getUser();
                    return userMapper.convertUserEntityToUserSummaryResponseDto(user);
                })
                .toList();

        return TheatreAdminResponseDTO.builder()
                .theatreId(theatre.getTheatreId())
                .theatreName(theatre.getTheatreName())
                .theatreAdmins(adminDtos)
                .build();
    }

    // Theatre Summary Response Dto -> Theatre Entity
    public Theatre convertTheatreSummaryResponseDtoToTheatreEntity(TheatreSummaryResponseDTO theatreSummaryResponseDTO) {
        return Theatre.builder()
                .theatreId(theatreSummaryResponseDTO.getTheatreId())
                .theatreName(theatreSummaryResponseDTO.getTheatreName())
                .theatreLocation(theatreSummaryResponseDTO.getTheatreLocation())
                .build();
    }

    // Theatre Response Dto -> Theatre Entity
    public Theatre convertTheatreResponseDtoToTheatreEntity(TheatreResponseDTO theatreResponseDTO) {

        List<Show> shows = theatreResponseDTO.getShows().stream()
                .map(showMapper::convertShowSummaryResponseDtoToShowEntity)
                .toList();

        return Theatre.builder()
                .theatreId(theatreResponseDTO.getTheatreId())
                .theatreName(theatreResponseDTO.getTheatreName())
                .theatreLocation(theatreResponseDTO.getTheatreLocation())
                .totalScreens(theatreResponseDTO.getTotalScreens())
                .shows(shows)
                .build();
    }

}
