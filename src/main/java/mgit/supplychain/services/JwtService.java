package mgit.supplychain.services;

import java.time.Instant;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import io.smallrye.jwt.build.Jwt;

@ApplicationScoped
public class JwtService {

    public String generateToken(
            String email,
            Set<String> roles) {

        return Jwt.subject(email)
                .groups(roles)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(900))
                .sign();
    }
}
