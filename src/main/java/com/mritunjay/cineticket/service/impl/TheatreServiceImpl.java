package com.mritunjay.cineticket.service.impl;

import com.mritunjay.cineticket.constants.ExceptionConstants;
import com.mritunjay.cineticket.dto.theatre.*;
import com.mritunjay.cineticket.dto.user.TheatreAdminRequestDTO;
import com.mritunjay.cineticket.exception.TheatreNotFoundException;
import com.mritunjay.cineticket.mapper.theatre.TheatreMapper;
import com.mritunjay.cineticket.mapper.user.UserMapper;
import com.mritunjay.cineticket.model.Screen;
import com.mritunjay.cineticket.model.Theatre;
import com.mritunjay.cineticket.model.TheatreVsAdmin;
import com.mritunjay.cineticket.model.User;
import com.mritunjay.cineticket.repository.TheatreRepository;
import com.mritunjay.cineticket.service.ScreenService;
import com.mritunjay.cineticket.service.TheatreService;
import com.mritunjay.cineticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;
    private final UserService userService;
    private final ScreenService screenService;

    private final TheatreMapper theatreMapper;
    private final UserMapper userMapper;

    @Autowired
    TheatreServiceImpl(TheatreRepository theatreRepository, UserService userService, ScreenService screenService, TheatreMapper theatreMapper, UserMapper userMapper) {
        this.theatreRepository = theatreRepository;
        this.userService = userService;
        this.screenService = screenService;
        this.theatreMapper = theatreMapper;
        this.userMapper = userMapper;
    }

    @Autowired
    public Page<TheatreSummaryResponseDTO> getAllTheatres(int page, int pageSize) {
        Page<Theatre> theatres = theatreRepository.findAll(PageRequest.of(page, pageSize));
        return theatres.map(theatreMapper::convertTheatreEntityToTheatreSummaryResponseDto);
    }

    @Override
    public TheatreResponseDTO getTheatreById(Long theatreId) {
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));

        return theatreMapper.convertTheatreEntityToTheatreResponseEntity(theatre);
    }

    @Override
    public TheatreDetailedResponseDTO createNewTheatre(TheatreRequestDTO theatreRequestDTO) {
        // Step 1: Fetch user who will become theatre admin
        User theatreAdmin = userMapper.convertUserResponseDtoToUserEntity(
                userService.getUserById(theatreRequestDTO.getTheatreAdminId())
        );

        // Step 2: Build the theatre, do not save yet
        Theatre newTheatre = Theatre
                .builder()
                .theatreName(theatreRequestDTO.getTheatreName())
                .theatreLocation(theatreRequestDTO.getTheatreLocation())
                .totalBookings(0)
                .totalRevenue(0D)
                .build();

        // Step 3: Assign admin mapping
        TheatreVsAdmin theatreVsAdmin = createTheatreVsAdmin(newTheatre, theatreAdmin);
        newTheatre.setTheatreAdmins(List.of(theatreVsAdmin));

        // Promote the user's role
        userService.promoteUser(theatreAdmin);

        List<Screen> screens = theatreRequestDTO.getScreens()
                .stream()
                .map(screenRequestDTO -> screenService.createNewScreen(newTheatre, screenRequestDTO))
                .toList();


        newTheatre.setScreens(screens);
        newTheatre.setTotalScreens(screens.size());

        // Step 4: Save theatre (cascades all)
        Theatre savedTheatre = theatreRepository.save(newTheatre);

        return theatreMapper.convertTheatreEntityToTheatreDetailedResponseDto(savedTheatre);
    }

    @Override
    public TheatreDetailedResponseDTO updateTheatreById(Long theatreId, TheatreRequestDTO theatreRequestDTO) {
        Theatre updatedTheatre = theatreRepository
                .findById(theatreId)
                .map(theatre -> {
                    theatre.setTheatreName(theatreRequestDTO.getTheatreName());
                    theatre.setTheatreLocation(theatreRequestDTO.getTheatreLocation());
                    return theatreRepository.save(theatre);
                })
                .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));

        return theatreMapper.convertTheatreEntityToTheatreDetailedResponseDto(updatedTheatre);
    }

    @Override
    public void deleteTheatreById(Long theatreId) {
        theatreRepository.deleteById(theatreId);
    }

    @Override
    public TheatreAdminResponseDTO addTheatreAdmin(TheatreAdminRequestDTO theatreAdminRequestDTO) {
        User theatreAdmin = userMapper.convertUserResponseDtoToUserEntity(
                userService.getUserById(theatreAdminRequestDTO.getUserId())
        );

        Theatre fetchedTheatre = theatreRepository
                .findById(theatreAdminRequestDTO.getTheatreId())
                .map(theatre -> {
                    List<TheatreVsAdmin> theatreAdmins = theatre.getTheatreAdmins();
                    TheatreVsAdmin theatreVsAdmin = createTheatreVsAdmin(theatre, theatreAdmin);
                    theatreAdmins.add(theatreVsAdmin);
                    theatre.setTheatreAdmins(theatreAdmins);
                    userService.promoteUser(theatreAdmin);

                    return theatreRepository.save(theatre);
                })
                .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));

        return theatreMapper.convertTheatreEntityToTheatreAdminResponseDto(fetchedTheatre);
    }

    @Override
    public TheatreAdminResponseDTO removeTheatreAdmin(TheatreAdminRequestDTO theatreAdminRequestDTO) {
        User theatreAdmin = userMapper.convertUserResponseDtoToUserEntity(
                userService.getUserById(theatreAdminRequestDTO.getUserId())
        );

        Theatre fetchedTheatre = theatreRepository
                .findById(theatreAdminRequestDTO.getTheatreId())
                .map(theatre -> {
                    List<TheatreVsAdmin> theatreAdmins = theatre.getTheatreAdmins();
                    TheatreVsAdmin theatreVsAdmin = createTheatreVsAdmin(theatre, theatreAdmin); // builds a TheatreVsAdmin object which has to be removed
                    theatreAdmins.remove(theatreVsAdmin);
                    theatre.setTheatreAdmins(theatreAdmins);

                    return theatreRepository.save(theatre);
                })
                .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));

        return theatreMapper.convertTheatreEntityToTheatreAdminResponseDto(fetchedTheatre);
    }

    @Override
    public void updateTheatre(Theatre theatre) {
        theatreRepository.save(theatre);
    }

    public TheatreVsAdmin createTheatreVsAdmin(Theatre theatre, User theatreAdmin) {
        return TheatreVsAdmin
                .builder()
                .theatre(theatre)
                .user(theatreAdmin)
                .build();
    }

}
