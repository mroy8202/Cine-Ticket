package com.mritunjay.cineticket.repository;

import com.mritunjay.cineticket.enums.SeatStatus;
import com.mritunjay.cineticket.model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
    List<ShowSeat> findByShowSeatIdInAndSeatStatus(
            List<Long> showSeatIds,
            SeatStatus seatStatus
    );
}
