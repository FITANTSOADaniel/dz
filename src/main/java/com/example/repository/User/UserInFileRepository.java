package com.example.repository.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.Qualifier.FileQualifier;
import com.example.models.Entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;

@FileQualifier 
@ApplicationScoped 
public class UserInFileRepository implements UserRepository{

    Path path = Path.of("user.json");
    ObjectMapper objectMapper = new ObjectMapper();
    
    void createFile(){
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la création du fichier : " + e.getMessage());
        }
    }
    
    @Override
    public User save(User user){
        this.createFile();
        try {
            List<User> users;
            
            if(Files.size(path) ==0){
                users = new ArrayList<>();
            } else{
                users = objectMapper.readValue(path.toFile(),
                 objectMapper.getTypeFactory()
                 .constructCollectionType(List.class, User.class));
            }

            int nextId = users.stream()
                    .mapToInt(User::getId)
                    .max()
                    .orElse(0) + 1;

            user.setId(nextId);

            users.add(user);

            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(path.toFile(), users);
                
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
        return user;
    }

    @Override
    public List<User> getAll(){
        try {
            if (!Files.exists(path) || Files.size(path) == 0) {
                return new ArrayList<>();
            }

            return objectMapper.readValue(
                path.toFile(),
                objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, User.class)
            );

        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<User> findById(Integer id) {

        List<User> users = getAll();

        User userFind =  users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
        if(userFind != null){
            return Optional.of(userFind);
        }
        return Optional.empty();
    }

    @Override
    public void deleteUser(String id){
        try {
            List<User> users = getAll();

            boolean removed = users.removeIf(user -> user.getId() == Integer.parseInt(id));

            if(!removed){
                System.out.println("Utilisateur introuvable");
                return;
            }

            try {
                objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(path.toFile(), users);
            } catch (Exception e) {
                System.out.println("Erreur lors de la suppression : "+ e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression : "+ e.getMessage());
        }
    }

    @Override
    public User updateUser(User newUser, String id){
        try {
            List<User> users = getAll();
            User user = users.stream()
                    .filter(u -> u.getId() == Integer.parseInt(id))
                    .findFirst()
                    .orElse(null);

            if (user == null) {
                System.out.println("Utilisateur introuvable.");
            }

            user.setFirstName(newUser.getFirstName());
            user.setName(newUser.getName());
            user.setPhone(newUser.getPhone());

            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(path.toFile(), users);
        } catch (Exception e) {
            System.out.println("Erreur lors de la modification : "+ e.getMessage());
        }
        return newUser;
    }
}
