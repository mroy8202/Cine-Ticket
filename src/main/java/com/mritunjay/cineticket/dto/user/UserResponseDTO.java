package com.mritunjay.cineticket.dto.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponseDTO {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String userEmail;
    private String userStatus; // enum -> String
    private LocalDateTime userCreatedAt;
    private LocalDateTime userUpdatedAt;
    private String userRole; // // enum -> String
}
