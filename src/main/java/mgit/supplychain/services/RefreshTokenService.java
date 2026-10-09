
package mgit.supplychain.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import mgit.supplychain.entity.RefreshToken;
import mgit.supplychain.entity.User;
import mgit.supplychain.repository.RefreshTokenRepository;

@ApplicationScoped
public class RefreshTokenService {

    private static final long REFRESH_TTL_SECONDS =
            7L * 24 * 60 * 60;

    @Inject
    RefreshTokenRepository repository;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public String create(User user) {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        // Valeur secrète envoyée une seule fois au client
        String rawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

        RefreshToken entity = new RefreshToken();
        entity.tokenHash = hash(rawToken);
        entity.user = user;
        entity.createdAt = Instant.now();
        entity.expiresAt = Instant.now()
                .plusSeconds(REFRESH_TTL_SECONDS);

        repository.persist(entity);

        return rawToken;
    }

    public String hash(String token) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] result = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(result);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Impossible de calculer le hash", e
            );
        }
    }
}