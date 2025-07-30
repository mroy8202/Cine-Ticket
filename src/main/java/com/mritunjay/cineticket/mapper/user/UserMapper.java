package com.mritunjay.cineticket.mapper.user;

import com.mritunjay.cineticket.dto.user.UserRequestDTO;
import com.mritunjay.cineticket.dto.user.UserResponseDTO;
import com.mritunjay.cineticket.dto.user.UserSummaryResponseDTO;
import com.mritunjay.cineticket.enums.UserRole;
import com.mritunjay.cineticket.enums.UserStatus;
import com.mritunjay.cineticket.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // User Request DTO -> User Entity
    public User convertUserRequestDtoToUserEntity(UserRequestDTO userRequestDTO) {
        return User.builder()
                .username(userRequestDTO.getUsername())
                .password(userRequestDTO.getPassword())
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .userEmail(userRequestDTO.getUserEmail())
                .build();
    }

    // User Entity -> UserResponseDTO
    public UserResponseDTO convertUserEntityToUserResponseDto(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userEmail(user.getUserEmail())
                .userStatus(user.getUserStatus().toString())
                .userCreatedAt(user.getUserCreatedAt())
                .userUpdatedAt(user.getUserUpdatedAt())
                .userRole(user.getUserRole().toString())
                .build();
    }

    // User Entity -> User Summary Response Dto
    public UserSummaryResponseDTO convertUserEntityToUserSummaryResponseDto(User user) {
        return UserSummaryResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .build();
    }

    // User Response Dto -> User Entity
    public User convertUserResponseDtoToUserEntity(UserResponseDTO userResponseDTO) {
        return User.builder()
                .userId(userResponseDTO.getUserId())
                .username(userResponseDTO.getUsername())
                .firstName(userResponseDTO.getFirstName())
                .lastName(userResponseDTO.getLastName())
                .userEmail(userResponseDTO.getUserEmail())
                .userStatus(UserStatus.valueOf(userResponseDTO.getUserStatus()))
                .userCreatedAt(userResponseDTO.getUserCreatedAt())
                .userUpdatedAt(userResponseDTO.getUserUpdatedAt())
                .userRole(UserRole.valueOf(userResponseDTO.getUserRole()))
                .build();
    }

}
