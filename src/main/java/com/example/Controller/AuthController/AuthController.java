package com.example.Controller.AuthController;

import com.example.dto.LoginDTO;
import com.example.dto.TokenResponse;
import com.example.dto.UserDTO;
import com.example.models.Entity.User;
import com.example.services.AuthService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces (MediaType.APPLICATION_JSON)
public class AuthController {
    private AuthService authService;

    public AuthController() {}
    
    @Inject
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @POST
    @Path("/register")
    public Response register(UserDTO userDTO) {
        User user = authService.register(userDTO);
        return Response
                .status(Response.Status.CREATED)
                .entity(user)
                .build();
    }

    @POST 
    @Path("/login")
    public Response login(LoginDTO loginDTO) {
        String token = authService.login(loginDTO);

        return Response.ok(
                new TokenResponse(token)
        ).build();
    }
}
