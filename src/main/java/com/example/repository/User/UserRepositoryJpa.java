package com.example.repository.User;

import java.util.List;
import java.util.Optional;

import com.example.Qualifier.JpaQualifier;
import com.example.models.Entity.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

@JpaQualifier 
@ApplicationScoped
public class UserRepositoryJpa implements UserRepository, FindUser{

    public UserRepositoryJpa() {}

    private EntityManager entityManager;

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
    public Optional<User> findByEmail(String email) {
        return entityManager
                .createQuery(
                    "SELECT u FROM User u WHERE u.email = :email",
                    User.class
                )
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Override
    public User save(User user){
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            entityManager.persist(user);
            entityManager.flush();

            transaction.commit();

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
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
        User user = entityManager.find(User.class, Integer.parseInt(id));

        if (user != null) {
            entityManager.remove(user);
        }
    }
    
    @Override
    public User updateUser(User newUser, String id){
        User user = entityManager.find(User.class, Integer.parseInt(id));
        user.setBalance(newUser.getBalance());
        user.setEmail(newUser.getEmail());
        user.setFirstName(newUser.getFirstName());
        user.setName(newUser.getName());
        user.setPhone(newUser.getPhone());
        return user;
    }
}
