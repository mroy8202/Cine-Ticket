package com.mritunjay.cineticket.service;

import com.mritunjay.cineticket.constants.ExceptionConstants;
import com.mritunjay.cineticket.dto.screen.ScreenRequestDTO;
import com.mritunjay.cineticket.dto.theatre.TheatreRequestDTO;
import com.mritunjay.cineticket.dto.user.TheatreAdminRequestDTO;
import com.mritunjay.cineticket.exception.TheatreNotFoundException;
import com.mritunjay.cineticket.model.Screen;
import com.mritunjay.cineticket.model.Theatre;
import com.mritunjay.cineticket.model.TheatreVsAdmin;
import com.mritunjay.cineticket.model.User;
import com.mritunjay.cineticket.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TheatreService {

    private final TheatreRepository theatreRepository;
    private final UserService userService;
    private final ScreenService screenService;

    @Autowired
    TheatreService(TheatreRepository theatreRepository, UserService userService, ScreenService screenService) {
        this.theatreRepository = theatreRepository;
        this.userService = userService;
        this.screenService = screenService;
    }

    public Page<Theatre> getAllTheatres(int page, int pageSize) {
        return theatreRepository.findAll(PageRequest.of(page, pageSize));
    }

    public Theatre getTheatreById(Long theatreId) {
        return theatreRepository
                .findById(theatreId)
                .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public Theatre createNewTheatre(TheatreRequestDTO theatreRequestDTO) {
        // Step 1: Fetch user who will become theatre admin
        User theatreAdmin = userService.getUserById(theatreRequestDTO.getTheatreAdminId());

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

        // Step 4: Create Screens and their Seats
        List<Screen> screens = new ArrayList<>();
        for (ScreenRequestDTO screenRequestDTO: theatreRequestDTO.getScreens()) {
            Screen screen = screenService.createNewScreen(newTheatre, screenRequestDTO);
            screens.add(screen);
        }
        newTheatre.setScreens(screens);
        newTheatre.setTotalScreens(screens.size());

        // Step 4: Save theatre (cascades all)
        return theatreRepository.save(newTheatre);
    }

    public Theatre updateTheatreById(Long theatreId, TheatreRequestDTO theatreRequestDTO) {
        return theatreRepository
                .findById(theatreId)
                .map(theatre -> {
                    theatre.setTheatreName(theatreRequestDTO.getTheatreName());
                    theatre.setTheatreLocation(theatreRequestDTO.getTheatreLocation());

                    return theatreRepository.save(theatre);
                })
                .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public Theatre addTheatreAdmin(TheatreAdminRequestDTO theatreAdminRequestDTO) {
        User theatreAdmin = userService.getUserById(theatreAdminRequestDTO.getUserId());

        return theatreRepository
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
    }

    public Theatre removeTheatreAdmin(TheatreAdminRequestDTO theatreAdminRequestDTO) {
        User theatreAdmin = userService.getUserById(theatreAdminRequestDTO.getUserId());

        return theatreRepository
                .findById(theatreAdminRequestDTO.getTheatreId())
                .map(theatre -> {
                    List<TheatreVsAdmin> theatreAdmins = theatre.getTheatreAdmins();
                    TheatreVsAdmin theatreVsAdmin = createTheatreVsAdmin(theatre, theatreAdmin);
                    theatreAdmins.remove(theatreVsAdmin);
                    theatre.setTheatreAdmins(theatreAdmins);

                    return theatreRepository.save(theatre);
                })
                .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public TheatreVsAdmin createTheatreVsAdmin(Theatre theatre, User theatreAdmin) {
        return TheatreVsAdmin
                .builder()
                .theatre(theatre)
                .user(theatreAdmin)
                .build();
    }

    public void deleteTheatreById(Long theatreId) {
        theatreRepository.deleteById(theatreId);
    }

    public void updateTheatre(Theatre theatre) {
        theatreRepository.save(theatre);
    }

}
