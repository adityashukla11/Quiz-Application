package com.quizapp.com.security.usermanagement.repository;

import com.quizapp.com.security.model.Role;
import com.quizapp.com.security.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<UserRole,Long> {
    Optional<UserRole> findByRole(Role role);
}
