package com.example.repository.Strategy;

import com.example.repository.User.UserInDatabaseRepository;
import com.example.repository.User.UserRepository;

public class SaveInDatabase implements StrategySaving{
    @Override
    public UserRepository chooseRepository(){
        return new UserInDatabaseRepository();
    }
}
