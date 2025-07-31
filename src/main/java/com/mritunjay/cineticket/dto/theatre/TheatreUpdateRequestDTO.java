package com.mritunjay.cineticket.dto.theatre;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class TheatreUpdateRequestDTO {
    Optional<String> theatreName = Optional.empty();
    Optional<String> theatreLocation = Optional.empty();
}
