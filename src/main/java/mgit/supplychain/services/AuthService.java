package mgit.supplychain.services;

import java.time.Instant;
import java.util.Set;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import mgit.supplychain.dto.LoginDTO;
import mgit.supplychain.dto.LoginResponseDTO;
import mgit.supplychain.dto.RefreshRequestDTO;
import mgit.supplychain.entity.RefreshToken;
import mgit.supplychain.entity.User;
import mgit.supplychain.repository.RefreshTokenRepository;
import mgit.supplychain.repository.UserRepository;

@ApplicationScoped 
public class AuthService {
    @Inject
    UserRepository userRepository;

    @Inject
    JwtService jwtService;

    @Inject
    RefreshTokenService refreshTokenService;

    @Inject
    RefreshTokenRepository refreshTokenRepository;


    public LoginResponseDTO login(LoginDTO dto) {

        // 1. Rechercher l'utilisateur dans PostgreSQL
        User user = userRepository.find("email", dto.getEmail()).firstResult();

        // 2. Vérifier l'existence et le mot de passe
        if (user == null || !BcryptUtil.matches(dto.getPassword(),user.getPasswordHash())) {
            throw new WebApplicationException(
                "Email ou mot de passe incorrect",
                Response.Status.UNAUTHORIZED
            );
        }

        // 3. Récupérer le rôle réel depuis la base
        Set<String> roles = Set.of(user.getRole().name());

        // 4. Générer le JWT
        String token = jwtService.generateToken(
            user.getEmail(),
            roles
        );

        String refreshToken = refreshTokenService.create(user);

        // 5. Construire la réponse
        return new LoginResponseDTO(
            token,
            refreshToken,
            "Bearer",
            900
        );
    }
    @Transactional
    public LoginResponseDTO refresh(RefreshRequestDTO dto) {

        // 1. Hasher le token reçu pour le rechercher en base
        String tokenHash = refreshTokenService.hash(dto.getRefreshToken());

        RefreshToken stored = refreshTokenRepository.find("tokenHash", tokenHash).firstResult();

        // 2. Vérifier que le token existe et est utilisable
        Instant now = Instant.now();

        if (stored == null || stored.revokedAt != null || !stored.expiresAt.isAfter(now)) {
            throw new WebApplicationException(
                "Refresh token invalide ou expiré",
                Response.Status.UNAUTHORIZED
            );
        }
        User user = stored.user;

        if (user.getRole() == null) {
            throw new WebApplicationException(
                "Rôle utilisateur manquant",
                Response.Status.INTERNAL_SERVER_ERROR
            );
        }

        // 3. Révoquer l'ancien refresh token
        stored.revokedAt = now;

        // 4. Générer un nouvel access token
        Set<String> roles = Set.of(user.getRole().name());

        String accessToken = jwtService.generateToken(user.getEmail(), roles);

        // 5. Rotation : créer un nouveau refresh token
        String newRefreshToken = refreshTokenService.create(user);

        // 6. Retourner les nouveaux tokens
        return new LoginResponseDTO(
            accessToken,
            newRefreshToken,
            "Bearer",
            900
        );
    }
}
