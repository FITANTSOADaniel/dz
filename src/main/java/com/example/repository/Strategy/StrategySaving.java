package com.example.repository.Strategy;

import com.example.repository.User.UserRepository;

public interface StrategySaving {
    UserRepository chooseRepository();
}
