package com.mritunjay.cineticket.service;

import com.mritunjay.cineticket.constants.ExceptionConstants;
import com.mritunjay.cineticket.dto.user.UserRequestDTO;
import com.mritunjay.cineticket.enums.UserRole;
import com.mritunjay.cineticket.enums.UserStatus;
import com.mritunjay.cineticket.exception.UserConflictException;
import com.mritunjay.cineticket.exception.UserNotFoundException;
import com.mritunjay.cineticket.model.User;
import com.mritunjay.cineticket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> getAllUsers(int page, int pageSize) {
        return userRepository.findAll(PageRequest.of(page, pageSize));
    }

    public User createNewUser(UserRequestDTO userRequestDTO) {
        if(userRepository.findByUsernameOrUserEmail(userRequestDTO.getUserName(), userRequestDTO.getUserEmail()).isPresent()) {
            throw new UserConflictException("User with the same username or email already exists", HttpStatus.CONFLICT);
        }

        User user = User.builder()
                .username(userRequestDTO.getUserName())
                .password(userRequestDTO.getPassword())
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .userEmail(userRequestDTO.getUserEmail())
                .userStatus(UserStatus.ACTIVE)
                .userCreatedAt(LocalDateTime.now())
                .userUpdatedAt(LocalDateTime.now())
                .userRole(UserRole.ROLE_USER)
                .build();

        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ExceptionConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public User updateUserById(Long userId, UserRequestDTO userRequestDTO) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setFirstName(userRequestDTO.getFirstName());
                    user.setLastName(userRequestDTO.getLastName());
                    user.setPassword(userRequestDTO.getPassword());
                    user.setUserUpdatedAt(LocalDateTime.now());

                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(ExceptionConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public User promoteUser(User user) {
        user.setUserRole(UserRole.ROLE_THEATRE_ADMIN);

        return userRepository.save(user);
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User getUserByUserName(String userName) {
        return userRepository
                .findByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException(ExceptionConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public boolean isUserPresentByUserNameOrUserEmail(String userName, String userEmail) {
        return userRepository.findByUsernameOrUserEmail(userName, userEmail).isPresent();
    }

}
