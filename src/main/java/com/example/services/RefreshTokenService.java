package com.example.services;

import com.example.models.Entity.*;
import com.example.repository.User.RefreshTokenRepositoryJpa;
import com.example.security.config.TokenHashService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;


@ApplicationScoped
public class RefreshTokenService {
    private static final int TOKEN_LENGTH = 32;
    private static final long REFRESH_TOKEN_DURATION_DAYS = 7;
    private SecureRandom secureRandom;
    private RefreshTokenRepositoryJpa repository;
    private TokenHashService tokenHashService;

    public RefreshTokenService(){}

    @Inject
    public RefreshTokenService( RefreshTokenRepositoryJpa repository, TokenHashService tokenHashService) {
        this.repository = repository;
        this.tokenHashService = tokenHashService;
        this.secureRandom = new SecureRandom();
    }

    public String generate() {
        byte[] bytes = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    public String create(User user) {
        String rawToken = generate();
        String tokenHash = tokenHashService.hash(rawToken);
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setTokenHash(tokenHash);
        refreshToken.setUser(user);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiresAt(Instant.now().plus(REFRESH_TOKEN_DURATION_DAYS,ChronoUnit.DAYS));

        repository.save(refreshToken);
        return rawToken;
    }

    public RefreshToken validate(String rawToken) {
        String tokenHash = tokenHashService.hash(rawToken);
        RefreshToken refreshToken = repository.findByTokenHash(tokenHash);
        if (refreshToken == null) {
            return null;
        }
        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            repository.delete(refreshToken);
            return null;
        }
        return refreshToken;
    }

    public void revoke(String rawToken) {
        String tokenHash = tokenHashService.hash(rawToken);

        RefreshToken refreshToken = repository.findByTokenHash(tokenHash);

        if (refreshToken != null) {
            repository.delete(refreshToken);
        }
    }
}