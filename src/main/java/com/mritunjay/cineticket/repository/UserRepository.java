package com.mritunjay.cineticket.repository;

import com.mritunjay.cineticket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserNameOrUserEmail(String userName, String userEmail);
    Optional<User> findByUserName(String userName);
}
