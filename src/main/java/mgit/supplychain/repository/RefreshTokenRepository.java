package mgit.supplychain.repository;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import mgit.supplychain.entity.RefreshToken;

@ApplicationScoped
public class RefreshTokenRepository
        implements PanacheRepositoryBase<RefreshToken, Long> {
}