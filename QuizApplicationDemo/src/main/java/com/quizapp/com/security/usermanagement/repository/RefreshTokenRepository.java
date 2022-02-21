package com.quizapp.com.security.usermanagement.repository;

import com.quizapp.com.security.model.RefreshToken;
import com.quizapp.com.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findById(Long id);
    Optional<RefreshToken> findByAuthToken(String token);
    Optional<Set<RefreshToken>> findAllByEatAfter(Instant instant);
    void deleteByUser(User user);
}
