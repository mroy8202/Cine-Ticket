package com.mritunjay.cineticket.service;

import com.mritunjay.cineticket.dto.reservation.ReservationRequestDTO;
import com.mritunjay.cineticket.dto.reservation.ReservationResponseDTO;
import com.mritunjay.cineticket.model.Reservation;
import org.springframework.data.domain.Page;

public interface ReservationService {

    // Get All Reservations for users
    Page<ReservationResponseDTO> getAllReservationsForUser(Long userId, int page, int pageSize);

    // Create New Reservation
    ReservationResponseDTO createNewReservation(ReservationRequestDTO reservationRequestDTO);

    // Cancel Reservation
    boolean cancelReservation(Long reservationId);

    // Get Reservation By Id
    Reservation getReservationById(Long reservationId);

}
