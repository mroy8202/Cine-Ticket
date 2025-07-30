package com.mritunjay.cineticket.service.impl;

import com.mritunjay.cineticket.constants.ExceptionConstants;
import com.mritunjay.cineticket.dto.reservation.ReservationRequestDTO;
import com.mritunjay.cineticket.dto.reservation.ReservationResponseDTO;
import com.mritunjay.cineticket.dto.showseat.ShowSeatRequestDTO;
import com.mritunjay.cineticket.enums.ReservationStatus;
import com.mritunjay.cineticket.exception.AvailableShowSeatsNotFoundException;
import com.mritunjay.cineticket.exception.ReservationNotCancellableException;
import com.mritunjay.cineticket.exception.ReservationNotFoundException;
import com.mritunjay.cineticket.mapper.reservation.ReservationMapper;
import com.mritunjay.cineticket.mapper.show.ShowMapper;
import com.mritunjay.cineticket.mapper.user.UserMapper;
import com.mritunjay.cineticket.model.*;
import com.mritunjay.cineticket.repository.ReservationRepository;
import com.mritunjay.cineticket.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.mritunjay.cineticket.constants.ExceptionConstants.AVAILABLE_SHOW_SEATS_NOT_FOUND;
import static com.mritunjay.cineticket.constants.ExceptionConstants.RESERVATION_NOT_CANCELLABLE;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final ShowService showService;
    private final TheatreService theatreService;
    private final ShowSeatService showSeatService;

    private final ReservationMapper reservationMapper;
    private final UserMapper userMapper;
    private final ShowMapper showMapper;

    public ReservationServiceImpl(ReservationRepository reservationRepository, UserService userService, ShowService showService, TheatreService theatreService, ShowSeatService showSeatService,ReservationMapper reservationMapper, UserMapper userMapper, ShowMapper showMapper) {
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.showService = showService;
        this.theatreService = theatreService;
        this.showSeatService = showSeatService;
        this.reservationMapper = reservationMapper;
        this.userMapper = userMapper;
        this.showMapper = showMapper;
    }

    @Override
    public Page<ReservationResponseDTO> getAllReservationsForUser(Long userId, int page, int pageSize) {
        Page<Reservation> allReservation = reservationRepository
                .findByUser_UserId(userId, PageRequest.of(page, pageSize));

        return allReservation.map(reservationMapper::convertReservationEntityToReservationResponseDto);
    }

    @Override
    public ReservationResponseDTO createNewReservation(ReservationRequestDTO reservationRequestDTO) {
        // Get all ShowSeat id's
        List<Long> showSeatIds = reservationRequestDTO
                .getShowSeats()
                .stream()
                .map(ShowSeatRequestDTO::getSeatId)
                .toList();

        List<ShowSeat> showSeats = showSeatService.getAvailableSeats(showSeatIds);

        showSeatService.acquireLocksForShowSeats(showSeatIds);

        if(showSeats.size() != showSeatIds.size()) {
            showSeatService.removeLocksForShowSeats(showSeatIds);
            throw new AvailableShowSeatsNotFoundException(AVAILABLE_SHOW_SEATS_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        User user = userMapper.convertUserResponseDtoToUserEntity(
                userService.getUserById(reservationRequestDTO.getUserId())
        );
        Show show = showMapper.convertShowDetailedResponseDtoToShowEntity(
                showService.getShowById(reservationRequestDTO.getShowId())
        );
        double totalAmount = showSeatService.bookSeatsAndReturnTotalAmount(showSeats);

        Theatre theatre = show.getTheatre();
        theatre.setTotalRevenue(theatre.getTotalRevenue() + totalAmount);
        theatre.setTotalBookings(theatre.getTotalBookings() + showSeats.size());
        theatreService.updateTheatre(theatre);

        Reservation reservation = Reservation
                .builder()
                .show(show)
                .user(user)
                .seatsReserved(showSeats)
                .reservationTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .reservationStatus(ReservationStatus.BOOKED)
                .totalAmount(totalAmount)
                .build();

        showSeatService.removeLocksForShowSeats(showSeatIds);

        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.convertReservationEntityToReservationResponseDto(savedReservation);
    }

    @Override
    public boolean cancelReservation(Long reservationId) {
        Reservation reservation = getReservationById(reservationId);

        if(reservation.getReservationStatus() == ReservationStatus.BOOKED) {
            if(!isReservationCancellable(reservation.getShow())) {
                throw new ReservationNotCancellableException(RESERVATION_NOT_CANCELLABLE, HttpStatus.NOT_ACCEPTABLE);
            }
            reservation.setReservationStatus(ReservationStatus.CANCELLED);
            reservation.setUpdatedTime(LocalDateTime.now());

            Theatre theatre = reservation.getShow().getTheatre();
            theatre.setTotalRevenue(theatre.getTotalRevenue() - reservation.getTotalAmount());
            theatre.setTotalBookings(theatre.getTotalBookings() - reservation.getSeatsReserved().size());
            theatreService.updateTheatre(theatre);

            reservationRepository.save(reservation);

            return true;
        }

        return false;
    }

    @Override
    public Reservation getReservationById(Long reservationId) {
        return reservationRepository
                .findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(ExceptionConstants.RESERVATION_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public boolean isReservationCancellable(Show show) {
        return show
                .getStartTime()
                .isAfter(LocalDateTime.now().minusMinutes(30));
    }

}
