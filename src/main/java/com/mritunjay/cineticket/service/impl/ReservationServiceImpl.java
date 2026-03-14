package com.mritunjay.cineticket.service.impl;

import com.mritunjay.cineticket.constants.ExceptionConstants;
import com.mritunjay.cineticket.dto.reservation.ReservationRequestDTO;
import com.mritunjay.cineticket.dto.reservation.ReservationResponseDTO;
import com.mritunjay.cineticket.dto.showseat.ShowSeatRequestDTO;
import com.mritunjay.cineticket.enums.ReservationStatus;
import com.mritunjay.cineticket.exception.*;
import com.mritunjay.cineticket.mapper.reservation.ReservationMapper;
import com.mritunjay.cineticket.model.*;
import com.mritunjay.cineticket.repository.ReservationRepository;
import com.mritunjay.cineticket.repository.ShowRepository;
import com.mritunjay.cineticket.repository.UserRepository;
import com.mritunjay.cineticket.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.mritunjay.cineticket.constants.ExceptionConstants.AVAILABLE_SHOW_SEATS_NOT_FOUND;
import static com.mritunjay.cineticket.constants.ExceptionConstants.RESERVATION_NOT_CANCELLABLE;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final TheatreService theatreService;
    private final ShowSeatService showSeatService;

    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(ReservationRepository reservationRepository, UserRepository userRepository, ShowRepository showRepository, TheatreService theatreService, ShowSeatService showSeatService,ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.theatreService = theatreService;
        this.showSeatService = showSeatService;
        this.reservationMapper = reservationMapper;
    }

    @Override
    public Page<ReservationResponseDTO> getAllReservationsForUser(Long userId, int page, int pageSize) {
        Page<Reservation> allReservation = reservationRepository
                .findByUser_UserId(userId, PageRequest.of(page, pageSize));

        return allReservation.map(reservationMapper::convertReservationEntityToReservationResponseDto);
    }

    @Override
    @Transactional
    public ReservationResponseDTO createNewReservation(ReservationRequestDTO reservationRequestDTO) {
        // Get all ShowSeat id's
        List<Long> showSeatIds = reservationRequestDTO
                .getShowSeats()
                .stream()
                .map(ShowSeatRequestDTO::getSeatId)
                .toList();

        showSeatService.acquireLocksForShowSeats(showSeatIds);
        List<ShowSeat> showSeats = showSeatService.getAvailableSeats(showSeatIds);

        if(showSeats.size() != showSeatIds.size()) {
            showSeatService.removeLocksForShowSeats(showSeatIds);
            throw new AvailableShowSeatsNotFoundException(AVAILABLE_SHOW_SEATS_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        User user = userRepository.findById(reservationRequestDTO.getUserId())
                .orElseThrow(() -> {
                    showSeatService.removeLocksForShowSeats(showSeatIds);
                    return new UserNotFoundException(ExceptionConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
                });

        Show show = showRepository.findById(reservationRequestDTO.getShowId())
                .orElseThrow(() -> {
                    showSeatService.removeLocksForShowSeats(showSeatIds);
                    return new ShowNotFoundException(ExceptionConstants.SHOW_NOT_FOUND, HttpStatus.NOT_FOUND);
                });

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

        Reservation savedReservation = reservationRepository.save(reservation);

        showSeatService.removeLocksForShowSeats(showSeatIds);

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
