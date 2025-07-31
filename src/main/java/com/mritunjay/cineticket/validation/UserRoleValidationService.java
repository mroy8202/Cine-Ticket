package com.mritunjay.cineticket.validation;

import com.mritunjay.cineticket.constants.ExceptionConstants;
import com.mritunjay.cineticket.enums.UserRole;
import com.mritunjay.cineticket.exception.*;
import com.mritunjay.cineticket.model.*;
import com.mritunjay.cineticket.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserRoleValidationService {

    private final UserRepository userRepository;
    private final TheatreRepository theatreRepository;
    private final ShowRepository showRepository;
    private final ScreenRepository screenRepository;
    private final ReservationRepository reservationRepository;

    public UserRoleValidationService(UserRepository userRepository, TheatreRepository theatreRepository, ShowRepository showRepository, ScreenRepository screenRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.theatreRepository = theatreRepository;
        this.showRepository = showRepository;
        this.screenRepository = screenRepository;
        this.reservationRepository = reservationRepository;
    }

    public boolean isSuperAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if(authority.getAuthority().equals(UserRole.ROLE_SUPER_ADMIN.name())) {
                return true;
            }
        }

        return false;
    }

    public boolean isTheatreAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if(authority.getAuthority().equals(UserRole.ROLE_THEATRE_ADMIN.name())) {
                return true;
            }
        }

        return false;
    }

    public boolean doesUserHavePermissionToPerformWriteOperationForTheatre(Long theatreId) {
        if(isSuperAdmin() || isTheatreAdmin()) {
            // check if user is super admin
            if(isSuperAdmin()) return true;

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            //User user = userService.getUserByUserName(currentUsername);
            User user = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new UserNotFoundException(ExceptionConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
            Theatre theatre = theatreRepository.findById(theatreId)
                    .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));

            // check if user is theatre admin of that particular theatre
            for (TheatreVsAdmin theatreAdmin : theatre.getTheatreAdmins()) {
                if(theatreAdmin.getUser().getUserId().equals(user.getUserId())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean doesUserHavePermissionToPerformWriteOperationForShow(Long showId) {
        if(isSuperAdmin() || isTheatreAdmin()) {
            if(isSuperAdmin()) return true;

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User user = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new UserNotFoundException(ExceptionConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

            Show show = showRepository.findById(showId)
                    .orElseThrow(() -> new ShowNotFoundException(ExceptionConstants.SHOW_NOT_FOUND, HttpStatus.NOT_FOUND));
            Theatre theatre = show.getTheatre();

            for (TheatreVsAdmin theatreAdmin : theatre.getTheatreAdmins()) {
                if(theatreAdmin.getUser().getUserId().equals(user.getUserId())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean doesUserHavePermissionToPerformWriteOperationForScreen(Long screenId) {
        if(isSuperAdmin() || isTheatreAdmin()) {
            if(isSuperAdmin()) return true;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User user = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new UserNotFoundException(ExceptionConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
            Screen screen = screenRepository.findById(screenId)
                    .orElseThrow(() -> new ScreenNotFoundException(ExceptionConstants.SCREEN_NOT_FOUND, HttpStatus.NOT_FOUND));
            Theatre theatre = screen.getTheatre();

            for (TheatreVsAdmin theatreAdmin : theatre.getTheatreAdmins()) {
                if(theatreAdmin.getUser().getUserId().equals(user.getUserId())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean doesUserHavePermissionToCancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotCancellableException(ExceptionConstants.RESERVATION_NOT_CANCELLABLE, HttpStatus.NOT_ACCEPTABLE));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException(ExceptionConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        return user.getUserId().equals(reservation.getUser().getUserId());
    }

}
