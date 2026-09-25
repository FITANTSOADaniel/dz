package com.example.Controller;
import com.example.repository.User.UserRepository;
import com.example.repository.User.UserInDatabaseRepository;
import com.example.repository.User.UserInFileRepository;

public class Stockagefactory {
    public static UserRepository create(String type){
        UserRepository userRepository;
        if(type.equals("db")){
            userRepository = new UserInDatabaseRepository();
        }else if (type.equals("file")) {
            userRepository = new UserInFileRepository();
        }else {
            throw new IllegalArgumentException(
                "Type de stockage inconnu : " + type
            );
        }
        return userRepository;
    }
}
