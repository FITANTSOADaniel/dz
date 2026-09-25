package com.example.repository.User;

import java.util.List;
import java.util.Optional;

import com.example.Qualifier.JpaQualifier;
import com.example.models.Entity.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@JpaQualifier 
@ApplicationScoped 
public class UserRepositoryJpa implements UserRepository{
    private final EntityManager entityManager;

    @Inject
    public UserRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(
            entityManager.find(User.class, id)
        );
    }

    @Override
    public User save(User user){
        entityManager.persist(user);
        entityManager.flush();

        return user;
    }
    @Override
    public List<User> getAll(){
        List<User> users = entityManager
            .createQuery("SELECT u FROM User u", User.class)
            .getResultList();
        return users;
    }
    @Override
    public void deleteUser(String id){

    }
    @Override
    public User updateUser(User newUser, String id){
        return new User();
    }
}
