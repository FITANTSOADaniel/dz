package mgit.supplychain.repository;
import mgit.supplychain.entity.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped 
public class UserRepository implements PanacheRepository<User> {
}
