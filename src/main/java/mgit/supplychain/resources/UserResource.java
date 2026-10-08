package mgit.supplychain.resources;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import mgit.supplychain.dto.CreateUserDTO;
import mgit.supplychain.dto.ResponseUserDTO;
import mgit.supplychain.entity.User;
import mgit.supplychain.services.UserService;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    UserService userService;

    @GET
    public List<ResponseUserDTO> findAll() {
        return userService.findAll();
    }

    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") Long id){
        return userService.findById(id)
                .map(user -> Response.ok(user).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST 
    public Response createUser(@Valid CreateUserDTO createUserDTO){
        User user = userService.createUser(createUserDTO);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @PUT
    @Path("{id}")
    public Response updateUser(@PathParam("id") Long id, @Valid CreateUserDTO createUserDTO){
        return userService.updateUser(id, createUserDTO)
                .map(user -> Response.ok(user).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id){
        boolean deleted = userService.delete(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
