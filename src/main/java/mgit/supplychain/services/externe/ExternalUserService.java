package mgit.supplychain.services.externe;

import java.util.List;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import mgit.supplychain.dto.ExternalUsers;

@ApplicationScoped
public class ExternalUserService {
    @Inject
    @RestClient
    ExternalService externalService;

    @Timeout(value= 3000)
    @Retry(maxRetries = 3, delay = 2000)
    @CircuitBreaker(
        requestVolumeThreshold = 4,
        failureRatio = 0.5,
        delay = 10000,
        successThreshold = 2
    )
    @Fallback(fallbackMethod = "fallback")
    public List<ExternalUsers> getUsers(){
        return externalService.getUsers();
    }

    private List<ExternalUsers> fallback() {
        throw new WebApplicationException(
            Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Le service externe est indisponible")
                .build()
        );
    }
}
