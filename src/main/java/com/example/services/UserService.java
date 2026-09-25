package com.example.services;

import java.util.List;
import java.util.Optional;

import com.example.Qualifier.JpaQualifier;
import com.example.dto.UserDTO;
import com.example.models.Entity.User;
import com.example.repository.User.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped 
public class UserService {
     
    private UserRepository userRepository;

    public UserService() {}
    
    @Inject
    public UserService(@JpaQualifier UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(UserDTO userDTO){
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        user.setBalance(userDTO.getBalance());
        return userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.getAll();
    }

    public void deleteUser(String id){
        userRepository.deleteUser(id);
    }

    public User updateUser(UserDTO userDTO, String id){
        User newUser = new User();
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setName(userDTO.getName());
        newUser.setPhone(userDTO.getPhone());
        newUser.setBalance(userDTO.getBalance());
        return userRepository.updateUser(newUser, id);
    }

    public Optional<User> findById(Integer id){
        return userRepository.findById(id);
    }
}
