package com.mritunjay.cineticket.config;

import com.mritunjay.cineticket.filter.JWTFilter;
import com.mritunjay.cineticket.service.auth.AppUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final AppUserDetailsService appUserDetailsService;
    private final JWTFilter jwtFilter;

    @Autowired
    SecurityConfig(AppUserDetailsService appUserDetailsService, JWTFilter jwtFilter) {
        this.appUserDetailsService = appUserDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(httpRequestCustomizer -> httpRequestCustomizer
                        .requestMatchers("/auth/**", "/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandlingCustomizer -> exceptionHandlingCustomizer
                        .authenticationEntryPoint(((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()))))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionManagementCustomizer -> sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headersCustomizer -> headersCustomizer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return appUserDetailsService;
    }

}
