package com.healthcare.user_service.repositories;

import com.healthcare.user_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    List<User> findByStatus(String status);
}
