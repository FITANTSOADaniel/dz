package com.example.services;

import com.example.Qualifier.JpaQualifier;
import com.example.dto.LoginDTO;
import com.example.dto.TokenResponse;
import com.example.dto.UserDTO;
import com.example.models.Entity.RefreshToken;
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
    private RefreshTokenService refreshTokenService;

    public AuthService() {}
    
    @Inject
    public AuthService(@JpaQualifier UserRepositoryJpa userRepository, PasswordHasher passwordHasher, JwtService jwtService, RefreshTokenService refreshTokenService){
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }
    
    @Transactional 
    public User register(UserDTO userDTO){
        if(userRepository.findByEmail(userDTO.getEmail()).isPresent()){
            throw new IllegalArgumentException(
                "Cette adresse email est déjà utilisée"
            );
        }

        String passwordHash = passwordHasher.hash(userDTO.getPassword());

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

    public TokenResponse login(LoginDTO loginDTO) {
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

        String refreshToken = refreshTokenService.create(user);

        String accessToken =  jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse refresh(String rawRefreshToken){
        RefreshToken oldRefreshToken = refreshTokenService.validate(rawRefreshToken);

        User user = oldRefreshToken.getUser();

        refreshTokenService.revoke(rawRefreshToken);

        String newAccessToken = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        String newRefreshToken = refreshTokenService.create(user);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
