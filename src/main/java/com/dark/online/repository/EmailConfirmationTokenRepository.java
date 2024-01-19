package com.dark.online.repository;

import com.dark.online.entity.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, String> {
    Optional<EmailConfirmationToken> findByToken(String token);
}
