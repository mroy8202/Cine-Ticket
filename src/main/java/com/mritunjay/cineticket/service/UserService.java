package com.mritunjay.cineticket.service;

import com.mritunjay.cineticket.dto.user.UserRequestDTO;
import com.mritunjay.cineticket.dto.user.UserResponseDTO;
import com.mritunjay.cineticket.model.User;
import org.springframework.data.domain.Page;

public interface UserService {

    // Get All Users
    Page<UserResponseDTO> getAllUsers(int page, int pageSize);

    // Create New User
    UserResponseDTO createNewUser(UserRequestDTO userRequestDTO);

    // Get User By id
    UserResponseDTO getUserById(Long userId);

    // Update User By Id
    UserResponseDTO updateUserById(Long userId, UserRequestDTO userRequestDTO);

    // Delete User By Id
    void deleteUserById(Long userId);

    // Promote User role
    void promoteUser(User user);

    // Demote User role
    void demoteUser(User user);

    // Get User By Username
    User getUserByUserName(String userName);

    // Is User Present By Username or UserEmail
    boolean isUserPresentByUserNameOrUserEmail(String userName, String userEmail);

}
