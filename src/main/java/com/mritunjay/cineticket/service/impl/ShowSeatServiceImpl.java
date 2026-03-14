package com.mritunjay.cineticket.service.impl;

import com.mritunjay.cineticket.constants.ExceptionConstants;
import com.mritunjay.cineticket.enums.SeatStatus;
import com.mritunjay.cineticket.exception.SeatAlreadyLockedException;
import com.mritunjay.cineticket.lock.SeatLock;
import com.mritunjay.cineticket.model.Seat;
import com.mritunjay.cineticket.model.Show;
import com.mritunjay.cineticket.model.ShowSeat;
import com.mritunjay.cineticket.repository.ShowSeatRepository;
import com.mritunjay.cineticket.service.ShowSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ShowSeatServiceImpl implements ShowSeatService {

    private final ShowSeatRepository showSeatRepository;
    private final SeatLock seatLock;

    @Autowired
    ShowSeatServiceImpl(ShowSeatRepository showSeatRepository, SeatLock seatLock) {
        this.showSeatRepository = showSeatRepository;
        this.seatLock = seatLock;
    }

    @Override
    public ShowSeat createNewShowSeat(Show show, Seat seat) {
        return ShowSeat
                .builder()
                .seat(seat)
                .seatStatus(SeatStatus.AVAILABLE)
                .show(show)
                .build();
    }

    @Override
    public List<ShowSeat> getShowSeats(List<Long> showSeatsIds) {
        return showSeatRepository.findAllById(showSeatsIds);
    }

    @Override
    public List<ShowSeat> getAvailableSeats(List<Long> showSeatsIds) {
        return showSeatRepository.findByShowSeatIdInAndSeatStatus(
                showSeatsIds,
                SeatStatus.AVAILABLE
        );
    }

    @Override
    public Double bookSeatsAndReturnTotalAmount(List<ShowSeat> showSeats) {
        Double amount = 0D;

        for (ShowSeat showSeat : showSeats) {
            showSeat.setSeatStatus(SeatStatus.BOOKED);
            amount += showSeat.getSeat().getSeatPrice();
        }

        showSeatRepository.saveAll(showSeats);
        return amount;
    }

    @Override
    public void acquireLocksForShowSeats(List<Long> showSeatIds) {
        List<Long> sortedIds = showSeatIds
                .stream()
                .sorted()
                .toList();

        for (Long showSeatId : sortedIds) {
            ReentrantLock reentrantLock = seatLock.getShowSeatLock(showSeatId);

            if(!reentrantLock.tryLock()) {
                releaseAcquiredLocks(sortedIds, showSeatId);
                throw new SeatAlreadyLockedException(ExceptionConstants.SEAT_ALREADY_LOCKED, HttpStatus.CONFLICT);
            }
        }
    }

    @Override
    public void removeLocksForShowSeats(List<Long> showSeatIds) {
        for (Long showSeatId : showSeatIds) {
            seatLock.removeLockForShowSeat(showSeatId);
        }
    }

    private void releaseAcquiredLocks(List<Long> sortedIds, Long failedAtId) {
        for(Long id : sortedIds) {
            if(id.equals(failedAtId)) break;

            ReentrantLock lock = seatLock.getShowSeatLock(id);
            if(lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
