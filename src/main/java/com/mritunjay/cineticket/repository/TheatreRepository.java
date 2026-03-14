package com.mritunjay.cineticket.repository;

import com.mritunjay.cineticket.model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {

    Optional<Theatre> findByTheatreNameAndTheatreLocation(
            String theatreName,
            String theatreLocation
    );

}
