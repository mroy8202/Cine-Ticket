package com.mritunjay.cineticket.controller;

import com.mritunjay.cineticket.dto.APIResponseDTO;
import com.mritunjay.cineticket.dto.PagedAPIResponseDTO;
import com.mritunjay.cineticket.dto.user.UserRequestDTO;
import com.mritunjay.cineticket.dto.user.UserResponseDTO;
import com.mritunjay.cineticket.model.User;
import com.mritunjay.cineticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Secured({"ROLE_SUPER_ADMIN"})
    @GetMapping("/all")
    public ResponseEntity<PagedAPIResponseDTO> getAllUsers(
            @RequestParam int page,
            @RequestParam int pageSize
    ) {
        Page<User> users = userService.getAllUsers(page, pageSize);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PagedAPIResponseDTO
                        .builder()
                        .pageData(users.getContent())
                        .totalElements(users.getTotalElements()) // total number of users
                        .totalPages(users.getTotalPages())
                        .currentLimit(users.getNumberOfElements())
                        .build()
                );
    }

    @Secured({"ROLE_SUPER_ADMIN"})
    @PostMapping("/user/create")
    public ResponseEntity<APIResponseDTO> createNewUser(
            @RequestBody UserRequestDTO userRequestDTO
    ) {

        String encodedPassword = bCryptPasswordEncoder.encode(userRequestDTO.getPassword());
        userRequestDTO.setPassword(encodedPassword);

        User newUser = userService.createNewUser(userRequestDTO);

        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .userId(newUser.getUserId())
                .userName(newUser.getUsername())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .userEmail(newUser.getUserEmail())
                .userStatus(newUser.getUserStatus())
                .userCreatedAt(newUser.getUserCreatedAt())
                .userUpdatedAt(newUser.getUserUpdatedAt())
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(APIResponseDTO.builder()
                        .message("New user created with id: " + newUser.getUserId() + " and email: " + newUser.getUserEmail())
                        .data(userResponseDTO)
                        .build()
                );
    }

    @Secured({"ROLE_SUPER_ADMIN"})
    @GetMapping("/user/{userId}")
    public ResponseEntity<APIResponseDTO> getUserById(
            @PathVariable Long userId
    ) {
        User user = userService.getUserById(userId);

        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUsername())
                .userEmail(user.getUserEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userStatus(user.getUserStatus())
                .userCreatedAt(user.getUserCreatedAt())
                .userUpdatedAt(user.getUserUpdatedAt())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIResponseDTO
                        .builder()
                        .data(userResponseDTO)
                        .build()
                );
    }

    @Secured({"ROLE_SUPER_ADMIN"})
    @PutMapping("/user/{userId}")
    public ResponseEntity<APIResponseDTO> updateUser(
            @PathVariable Long userId,
            @RequestBody UserRequestDTO userRequestDTO
    ) {
        User updatedUser = userService.updateUserById(userId, userRequestDTO);

        UserResponseDTO userResponseDTO = UserResponseDTO
                .builder()
                .userId(updatedUser.getUserId())
                .userName(updatedUser.getUsername())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .userEmail(updatedUser.getUserEmail())
                .userStatus(updatedUser.getUserStatus())
                .userCreatedAt(updatedUser.getUserCreatedAt())
                .userUpdatedAt(updatedUser.getUserUpdatedAt())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIResponseDTO
                        .builder()
                        .message("Updated the user with id: " + updatedUser.getUserId() + " and email: " + updatedUser.getUserEmail())
                        .data(userResponseDTO)
                        .build()
                );
    }

    @Secured({"ROLE_SUPER_ADMIN"})
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<APIResponseDTO> deleteUserById(
            @PathVariable Long userId
    ) {
        User deletedUser = userService.getUserById(userId);
        userService.deleteUserById(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIResponseDTO
                        .builder()
                        .message("Deleted the user with id: " + deletedUser.getUserId() + " and email: " + deletedUser.getUserEmail())
                        .build()
                );
    }

}
