package com.mritunjay.cineticket.controller.auth;

import com.mritunjay.cineticket.dto.auth.AuthRequestDTO;
import com.mritunjay.cineticket.dto.auth.AuthResponseDTO;
import com.mritunjay.cineticket.dto.user.UserRequestDTO;
import com.mritunjay.cineticket.service.auth.AuthenticationService;
import com.mritunjay.cineticket.service.auth.TokenBlacklist;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklist tokenBlacklist;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, AuthenticationManager authenticationManager, TokenBlacklist tokenBlacklist) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
        this.tokenBlacklist = tokenBlacklist;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signUpUser(
            @RequestBody UserRequestDTO userRequestDTO
    ) {
        String authenticationToken = authenticationService.singUpUser(userRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AuthResponseDTO
                        .builder()
                        .authenticationToken(authenticationToken)
                        .build()
                );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody AuthRequestDTO authRequestDTO
    ) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                authRequestDTO.getUserName(),
                authRequestDTO.getPassword()
        );

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        String authencationToken = authenticationService.generateTokenForUser(authRequestDTO.getUserName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AuthResponseDTO
                        .builder()
                        .authenticationToken(authencationToken)
                        .build()
                );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklist.blacklistToken(token);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Logged out successfully");
    }
}
