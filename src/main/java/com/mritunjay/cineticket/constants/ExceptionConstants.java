package com.mritunjay.cineticket.constants;

public interface ExceptionConstants {

    String USER_NOT_FOUND = "The user with specified id is not found";
    String USER_ALREADY_EXISTS = "The user with same username or user email already exists";

    String THEATRE_NOT_FOUND = "The theatre with specified id is not found.";
    String MOVIE_NOT_FOUND = "The movie with specified id is not found.";
    String SHOW_NOT_FOUND = "The show with specified id is not found.";
    String SCREEN_NOT_FOUND = "The screen with specified id is not found.";
    String RESERVATION_NOT_FOUND = "The reservation with specified id is not found.";
    String RESERVATION_NOT_CANCELLABLE = "The reservation with specified id is not cancellable as the time got exceeded.";
    String AVAILABLE_SHOW_SEATS_NOT_FOUND = "No available show seats found.";
    String SEAT_ALREADY_LOCKED = "One or more similar seats are already booked by another user.";
}
