package com.example.repository.Strategy;

import com.example.repository.User.UserInFileRepository;
import com.example.repository.User.UserRepository;

public class SaveInFile implements StrategySaving{
    @Override
    public UserRepository chooseRepository(){
        return new UserInFileRepository();
    }
}
