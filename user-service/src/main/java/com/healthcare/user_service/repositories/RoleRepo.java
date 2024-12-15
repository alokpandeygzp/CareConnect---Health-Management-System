package com.healthcare.user_service.repositories;

import com.healthcare.user_service.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);
}
