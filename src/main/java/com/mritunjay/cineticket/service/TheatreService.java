package com.mritunjay.cineticket.service;

import com.mritunjay.cineticket.dto.theatre.*;
import com.mritunjay.cineticket.dto.user.TheatreAdminRequestDTO;
import com.mritunjay.cineticket.model.Theatre;
import org.springframework.data.domain.Page;

public interface TheatreService {

    // Get All Theatres
    Page<TheatreSummaryResponseDTO> getAllTheatres(int page, int pageSize);

    // Get Theatre by id
    TheatreResponseDTO getTheatreById(Long theatreId);

    // Create New Theatre
    TheatreDetailedResponseDTO createNewTheatre(TheatreRequestDTO theatreRequestDTO);

    // Update Theatre By id
    TheatreDetailedResponseDTO updateTheatreById(Long theatreId, TheatreRequestDTO theatreRequestDTO);

    // Delete Theatre By id
    void deleteTheatreById(Long userId);

    // Add Theatre Admin
    TheatreAdminResponseDTO addTheatreAdmin(TheatreAdminRequestDTO theatreAdminRequestDTO);

    // Remove Theatre Admin
    TheatreAdminResponseDTO removeTheatreAdmin(TheatreAdminRequestDTO theatreAdminRequestDTO);

    // Update Theatre
    void updateTheatre(Theatre theatre);

}
