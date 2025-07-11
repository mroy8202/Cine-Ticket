package com.mritunjay.cineticket.dto.user;

import com.mritunjay.cineticket.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponseDTO {
    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String userEmail;
    private UserStatus userStatus;
    private LocalDateTime userCreatedAt;
    private LocalDateTime userUpdatedAt;
}
