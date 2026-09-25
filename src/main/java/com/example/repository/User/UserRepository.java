package com.example.repository.User;

import java.util.List;
import java.util.Optional;

import com.example.models.Entity.User;

public interface UserRepository {
    User save(User user);
    List<User> getAll();
    Optional<User> findById(Integer id);
    void deleteUser(String id);
    User updateUser(User newUser, String id);
}