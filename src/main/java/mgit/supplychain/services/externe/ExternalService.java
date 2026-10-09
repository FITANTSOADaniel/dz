package mgit.supplychain.services.externe;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import mgit.supplychain.dto.ExternalUsers;

@RegisterRestClient(configKey = "external-api")
@Path("/users/all")
public interface ExternalService {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ExternalUsers> getUsers();
}