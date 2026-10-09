package mgit.supplychain.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Readiness
@ApplicationScoped
public class DatabaseHealthCheck implements HealthCheck{
    @Inject
    AgroalDataSource dataSource;

    @Override
    public HealthCheckResponse call() {
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {

            statement.execute("SELECT 1");

            return HealthCheckResponse.named("database")
                    .up()
                    .build();

        } catch (Exception e) {
            return HealthCheckResponse.named("database")
                    .down()
                    .build();
        }
    }
}
