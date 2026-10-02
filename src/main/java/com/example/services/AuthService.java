package com.example.services;

import com.example.Qualifier.JpaQualifier;
import com.example.dto.LoginDTO;
import com.example.dto.UserDTO;
import com.example.models.Entity.User;
import com.example.repository.User.UserRepositoryJpa;
import com.example.security.config.PasswordHasher;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped 
public class AuthService {
    private UserRepositoryJpa userRepository;
    private PasswordHasher passwordHasher;
    private JwtService jwtService;

    public AuthService() {}
    
    @Inject
    public AuthService(@JpaQualifier UserRepositoryJpa userRepository, PasswordHasher passwordHasher, JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.jwtService = jwtService;
    }
    
    @Transactional 
    public User register(UserDTO userDTO){
        if(userRepository.findByEmail(userDTO.getEmail()).isPresent()){
            throw new IllegalArgumentException(
                "Cette adresse email est déjà utilisée"
            );
        }

        String passwordHash =
                passwordHasher.hash(userDTO.getPassword());

        // 3. Créer l'utilisateur
        User user = new User();

        user.setName(userDTO.getName());
        user.setFirstName(userDTO.getFirstName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setRole(userDTO.getRole());
        user.setPasswordHash(passwordHash);
        // 4. Enregistrer

        return userRepository.save(user);
    }

    public String login(LoginDTO loginDTO) {

        User user = userRepository
                .findByEmail(loginDTO.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                            "Email ou mot de passe incorrect"
                        )
                );

        boolean valid = passwordHasher.verify(
                loginDTO.getPassword(),
                user.getPasswordHash()
        );

        if (!valid) {
            throw new IllegalArgumentException(
                    "Email ou mot de passe incorrect"
            );
        }

        return jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
