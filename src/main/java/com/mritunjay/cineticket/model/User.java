package com.mritunjay.cineticket.model;

import com.mritunjay.cineticket.enums.UserRole;
import com.mritunjay.cineticket.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "app_users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "userEmail")
    }
)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;

    private String password;

    private String firstName;

    private String lastName;

    private String userEmail;

    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;

    private LocalDateTime userCreatedAt;

    private LocalDateTime userUpdatedAt;

    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

}
