package com.mritunjay.cineticket.filter;

import com.mritunjay.cineticket.model.User;
import com.mritunjay.cineticket.service.impl.UserServiceImpl;
import com.mritunjay.cineticket.service.auth.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserServiceImpl userService;

    @Autowired
    JWTFilter(JWTService jwtService, UserServiceImpl userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String userName = jwtService.extractUserNameFromJwt(jwt);

            if(userName != null && !userName.isEmpty()) {
                User user = userService.getUserByUserName(userName);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails((new WebAuthenticationDetailsSource().buildDetails(request)));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
