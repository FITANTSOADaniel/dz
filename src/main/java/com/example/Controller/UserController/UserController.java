package com.example.Controller.UserController;

import java.util.List;

import com.example.dto.UserDTO;
import com.example.models.Entity.User;
import com.example.services.UserService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    private UserService userService;

    public UserController() {}
    
    @Inject
    public UserController(UserService userService){
        this.userService = userService;
    }
    
    @GET
    @RolesAllowed("ADMIN")
    public List<User> getAll() {
        return userService.getAll();
    }

    @GET
    @Path("/{id}") 
    public Response getUser(@PathParam("id") Integer id) {
        return userService.findById(id)
                .map(user -> Response.ok(user).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST 
    public Response createUser(@Valid UserDTO userDTO){
        User user = userService.createUser(userDTO);

        return Response
                .status(Response.Status.CREATED)
                .entity(user)
                .build();
    }
}
