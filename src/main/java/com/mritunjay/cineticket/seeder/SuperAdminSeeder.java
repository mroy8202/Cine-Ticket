package com.mritunjay.cineticket.seeder;

import com.mritunjay.cineticket.enums.UserRole;
import com.mritunjay.cineticket.model.User;
import com.mritunjay.cineticket.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SuperAdminSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    public SuperAdminSeeder(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        loadSuperAdminUser();
    }

    private void loadSuperAdminUser() {
        User superAdmin = User
                .builder()
                .username("superAdmin")
                .password(bCryptPasswordEncoder.encode("superPassword@123"))
                .userEmail("superAdmin@gmail.com")
                .firstName("Super")
                .lastName("Admin")
                .userRole(UserRole.ROLE_SUPER_ADMIN)
                .build();

        userRepository.save(superAdmin);
    }

}
