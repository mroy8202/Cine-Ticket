package com.mritunjay.cineticket.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String userEmail;
}
