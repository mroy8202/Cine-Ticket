package com.mritunjay.cineticket.repository;

import com.mritunjay.cineticket.model.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    Page<Show> findByMovie_MovieId(Long movieId, Pageable pageable);

    Page<Show> findByTheatre_TheatreId(Long theatreId, Pageable pageable);

    Page<Show> findByScreen_ScreenId(Long screenId, Pageable pageable);
}
