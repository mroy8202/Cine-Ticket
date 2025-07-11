package com.mritunjay.cineticket.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class APIResponseDTO {
    String message;
    Object data;
}
