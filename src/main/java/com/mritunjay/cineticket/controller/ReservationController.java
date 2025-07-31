package com.mritunjay.cineticket.controller;

import com.mritunjay.cineticket.dto.APIResponseDTO;
import com.mritunjay.cineticket.dto.PagedAPIResponseDTO;
import com.mritunjay.cineticket.dto.reservation.ReservationRequestDTO;
import com.mritunjay.cineticket.dto.reservation.ReservationResponseDTO;
import com.mritunjay.cineticket.model.Reservation;
import com.mritunjay.cineticket.service.ReservationService;
import com.mritunjay.cineticket.service.impl.ReservationServiceImpl;
import com.mritunjay.cineticket.validation.UserRoleValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserRoleValidationService userRoleValidationService;

    @Autowired
    public ReservationController(ReservationService reservationService, UserRoleValidationService userRoleValidationService) {
        this.reservationService = reservationService;
        this.userRoleValidationService = userRoleValidationService;
    }

    @GetMapping("/user/{userId}/all")
    public ResponseEntity<PagedAPIResponseDTO> getAllReservations(
            @PathVariable Long userId,
            @RequestParam int page,
            @RequestParam int pageSize
    ) {
        Page<ReservationResponseDTO> reservations = reservationService.getAllReservationsForUser(userId, page, pageSize);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PagedAPIResponseDTO
                        .builder()
                        .pageData(reservations.getContent())
                        .totalElements(reservations.getTotalElements())
                        .totalPages(reservations.getTotalPages())
                        .currentLimit(reservations.getNumberOfElements())
                        .build()
                );
    }

    @Secured("ROLE_USER")
    @PostMapping("/reserve")
    public ResponseEntity<APIResponseDTO> createNewReservations(
            @RequestBody ReservationRequestDTO reservationRequestDTO
    ) {
        // need to verify if the current user is trying to create a reservation for another user

        ReservationResponseDTO newReservation = reservationService.createNewReservation(reservationRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(APIResponseDTO
                        .builder()
                        .message("New movie created with id: " + newReservation.getReservationId() + " and movie name: " + newReservation.getShow().getMovie().getMovieName())
                        .data(newReservation)
                        .build()
                );
    }

    @PreAuthorize("@userRoleValidationService.doesUserHavePermissionToCancelReservation(#reservationId)")
    @PutMapping("/cancel/{reservationId}")
    public ResponseEntity<APIResponseDTO> cancelReservation(
            @PathVariable Long reservationId
    ) {
        // need to verify if the current user have permission to cancel the reservation

        String message = "The reservation id: " + reservationId + " is already cancelled";

        if(reservationService.cancelReservation(reservationId)) {
            message = "Cancelled the reservation id: " + reservationId;
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(APIResponseDTO
                        .builder()
                        .message(message)
                        .build()
                );
    }

}
