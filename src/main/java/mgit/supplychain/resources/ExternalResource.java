package mgit.supplychain.resources;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import mgit.supplychain.dto.ExternalUsers;
import mgit.supplychain.services.externe.ExternalUserService;

@RolesAllowed("ADMIN")
@Path("/getExtUsers")
@Produces(MediaType.APPLICATION_JSON)
public class ExternalResource {
    @Inject
    ExternalUserService externalUserService;

    @GET
    public List<ExternalUsers> getAll(){
        return externalUserService.getUsers();
    }
}
