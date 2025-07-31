package com.mritunjay.cineticket.service.impl;

import com.mritunjay.cineticket.constants.ExceptionConstants;
import com.mritunjay.cineticket.dto.user.UserRequestDTO;
import com.mritunjay.cineticket.dto.user.UserResponseDTO;
import com.mritunjay.cineticket.enums.UserRole;
import com.mritunjay.cineticket.enums.UserStatus;
import com.mritunjay.cineticket.exception.UserConflictException;
import com.mritunjay.cineticket.exception.UserNotFoundException;
import com.mritunjay.cineticket.mapper.user.UserMapper;
import com.mritunjay.cineticket.model.User;
import com.mritunjay.cineticket.repository.UserRepository;
import com.mritunjay.cineticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Page<UserResponseDTO> getAllUsers(int page, int pageSize) {
        Page<User> allUsers = userRepository.findAll(PageRequest.of(page, pageSize));
        return allUsers.map(userMapper::convertUserEntityToUserResponseDto); // converted to Page<UserResponseDTO>
    }

    @Override
    public UserResponseDTO createNewUser(UserRequestDTO userRequestDTO) {
        if(userRepository.findByUsernameOrUserEmail(userRequestDTO.getUsername(), userRequestDTO.getUserEmail()).isPresent()) {
            throw new UserConflictException("User with the same username or email already exists", HttpStatus.CONFLICT);
        }

        // Convert UserRequestDTO to Entity
        User newUser = userMapper.convertUserRequestDtoToUserEntity(userRequestDTO);

        // set other fields in User entity
        newUser.setUserStatus(UserStatus.ACTIVE);
        newUser.setUserCreatedAt(LocalDateTime.now());
        newUser.setUserUpdatedAt(LocalDateTime.now());
        newUser.setUserRole(UserRole.ROLE_USER);

        // save the user in DB
        User savedUser = userRepository.save(newUser);

        // convert Entity to UserResponseDTO and return
        return userMapper.convertUserEntityToUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ExceptionConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        // convert the found User Entity to User Response DTO and return
        return userMapper.convertUserEntityToUserResponseDto(user);
    }

    @Override
    public UserResponseDTO updateUserById(Long userId, UserRequestDTO userRequestDTO) {
        // find the user who has to be updated
        User userToBeUpdated = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ExceptionConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        // update the fields
        userToBeUpdated.setFirstName(userRequestDTO.getFirstName());
        userToBeUpdated.setLastName(userRequestDTO.getLastName());
        userToBeUpdated.setPassword(userRequestDTO.getPassword());
        userToBeUpdated.setUserUpdatedAt(LocalDateTime.now());

        // save in db
        User updatedUser = userRepository.save(userToBeUpdated);

        // convert User Entity to User Response DTO and return
        return userMapper.convertUserEntityToUserResponseDto(updatedUser);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void promoteUser(User user) {
        user.setUserRole(UserRole.ROLE_THEATRE_ADMIN);
        userRepository.save(user);
    }

    @Override
    public void demoteUser(User user) {
        user.setUserRole(UserRole.ROLE_USER);
        userRepository.save(user);
    }

    @Override
    public User getUserByUserName(String userName) {
        return userRepository
                .findByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException(ExceptionConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public boolean isUserPresentByUserNameOrUserEmail(String userName, String userEmail) {
        return userRepository.findByUsernameOrUserEmail(userName, userEmail).isPresent();
    }

}
