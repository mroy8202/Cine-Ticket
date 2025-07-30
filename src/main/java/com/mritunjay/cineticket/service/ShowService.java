package com.mritunjay.cineticket.service;

import com.mritunjay.cineticket.dto.show.ShowDetailedResponseDTO;
import com.mritunjay.cineticket.dto.show.ShowRequestDTO;
import com.mritunjay.cineticket.dto.show.ShowResponseDTO;
import org.springframework.data.domain.Page;

public interface ShowService {

    // Get All Shows
    Page<ShowResponseDTO> getAllShows(int page, int pageSize);

    // Get All Shows By Movie
    Page<ShowResponseDTO> getShowsByMovieId(Long movieId, int page, int pageSize);

    // Get All Shows By Screen
    Page<ShowDetailedResponseDTO> getShowsByScreenId(Long screenId, int page, int pageSize);

    // Get All Shows By Theatre
    Page<ShowResponseDTO> getShowsByTheatreId(Long theatreId, int page, int pageSize);

    // Get Show By id
    ShowDetailedResponseDTO getShowById(Long showId);

    // Create New Show
    ShowDetailedResponseDTO createNewShow(ShowRequestDTO showRequestDTO);

    // Update Show By id
    ShowDetailedResponseDTO updateShowById(Long showId, ShowRequestDTO showRequestDTO);

    // Delete Show By id
    void deleteShowById(Long showId);

}
