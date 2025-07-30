package com.mritunjay.cineticket.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequestDTO {
    private String userName;
    private String password;
}
