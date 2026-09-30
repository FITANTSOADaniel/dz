package com.example.repository.User;

import java.util.Optional;

import com.example.models.Entity.User;

public interface FindUser {
    public Optional<User> findByEmail(String email);
}
