package com.mritunjay.cineticket.controller;

import com.mritunjay.cineticket.dto.APIResponseDTO;
import com.mritunjay.cineticket.dto.PagedAPIResponseDTO;
import com.mritunjay.cineticket.dto.theatre.*;
import com.mritunjay.cineticket.dto.user.TheatreAdminRequestDTO;
import com.mritunjay.cineticket.model.Theatre;
import com.mritunjay.cineticket.model.User;
import com.mritunjay.cineticket.service.TheatreService;
import com.mritunjay.cineticket.service.impl.TheatreServiceImpl;
import com.mritunjay.cineticket.service.UserService;
import com.mritunjay.cineticket.validation.UserRoleValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/theatres")
public class TheatreController {

    private final TheatreService theatreService;
    private final UserRoleValidationService userRoleValidationService;

    @Autowired
    public TheatreController(TheatreService theatreService, UserRoleValidationService userRoleValidationService) {
        this.theatreService = theatreService;
        this.userRoleValidationService = userRoleValidationService;
    }

    @GetMapping("/all")
    public ResponseEntity<PagedAPIResponseDTO> getAllTheatres(
            @RequestParam int page,
            @RequestParam int pageSize
    ) {
        Page<TheatreSummaryResponseDTO> theatres = theatreService.getAllTheatres(page, pageSize);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PagedAPIResponseDTO
                        .builder()
                        .pageData(theatres.getContent())
                        .totalElements(theatres.getTotalElements())
                        .totalPages(theatres.getTotalPages())
                        .currentLimit(theatres.getNumberOfElements())
                        .build()
                );
    }

    @GetMapping("/theatre/{theatreId}")
    public ResponseEntity<APIResponseDTO> getTheatreById(
            @PathVariable Long theatreId
    ) {
        TheatreResponseDTO theatre = theatreService.getTheatreById(theatreId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(APIResponseDTO
                        .builder()
                        .data(theatre)
                        .build()
                );
    }

    @Secured({"ROLE_SUPER_ADMIN"})
    @PostMapping("/theatre/create")
    public ResponseEntity<APIResponseDTO> createNewTheatre(
            @RequestBody TheatreRequestDTO theatreRequestDTO
    ) {
        TheatreDetailedResponseDTO newTheatre = theatreService.createNewTheatre(theatreRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(APIResponseDTO
                        .builder()
                        .message("New theatre created with id: " + newTheatre.getTheatreId() + " and name: " + newTheatre.getTheatreName())
                        .data(newTheatre)
                        .build()
                );
    }

    @PreAuthorize("@userRoleValidationService.doesUserHavePermissionToPerformWriteOperationForTheatre(#theatreId)")
    @PutMapping("/theatre/{theatreId}")
    public ResponseEntity<APIResponseDTO> updateTheatreById(
            @PathVariable Long theatreId,
            @RequestBody TheatreUpdateRequestDTO theatreUpdateRequestDTO
    ) {
        TheatreDetailedResponseDTO updatedTheatre = theatreService.updateTheatreById(theatreId, theatreUpdateRequestDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIResponseDTO
                        .builder()
                        .message("Updated the theatre with id: " + updatedTheatre.getTheatreId()+ " and name: " + updatedTheatre.getTheatreName())
                        .data(updatedTheatre)
                        .build()
                );
    }

    @PreAuthorize("@userRoleValidationService.doesUserHavePermissionToPerformWriteOperationForTheatre(#theatreId)")
    @DeleteMapping("/theatre/{theatreId}")
    public ResponseEntity<APIResponseDTO> deleteTheatreById(
            @PathVariable Long theatreId
    ) {
        TheatreResponseDTO deletedTheatre = theatreService.getTheatreById(theatreId);
        theatreService.deleteTheatreById(theatreId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIResponseDTO
                        .builder()
                        .message("Deleted the theatre with id: " + deletedTheatre.getTheatreId() + " and name: " + deletedTheatre.getTheatreName())
                        .build()
                );
    }

    @PreAuthorize("@userRoleValidationService.doesUserHavePermissionToPerformWriteOperationForTheatre(#theatreAdminRequestDTO.theatreId)")
    @PostMapping("/theatre/admin")
    public ResponseEntity<APIResponseDTO> addTheatreAdmin(
            @RequestBody TheatreAdminRequestDTO theatreAdminRequestDTO
    ) {
        TheatreAdminResponseDTO theatre = theatreService.addTheatreAdmin(theatreAdminRequestDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIResponseDTO
                        .builder()
                        .message("Added the theatre admin to the name: " + theatre.getTheatreName())
                        .build()
                );
    }

    @PreAuthorize("@userRoleValidationService.doesUserHavePermissionToPerformWriteOperationForTheatre(#theatreAdminRequestDTO.theatreId)")
    @DeleteMapping("/theatre/admin")
    public ResponseEntity<APIResponseDTO> removeTheatreAdmin(
            @RequestBody TheatreAdminRequestDTO theatreAdminRequestDTO
    ) {
        TheatreAdminResponseDTO theatre = theatreService.removeTheatreAdmin(theatreAdminRequestDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIResponseDTO
                        .builder()
                        .message("Removed the theatre admin from theatre name: " + theatre.getTheatreName())
                        .build()
                );
    }

}
