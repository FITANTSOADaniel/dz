package mgit.supplychain.resources;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import mgit.supplychain.dto.LoginDTO;
import mgit.supplychain.dto.LoginResponseDTO;
import mgit.supplychain.dto.RefreshRequestDTO;
import mgit.supplychain.services.AuthService;

@PermitAll
@Path("/api/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces (MediaType.APPLICATION_JSON)
public class AuthResource {
    @Inject
    AuthService authService;

    @POST
    @Path("login")
    @PermitAll 
    public LoginResponseDTO login(@Valid LoginDTO dto){
        return authService.login(dto);
    }

    @POST
    @Path("/refresh")
    public LoginResponseDTO refresh(@Valid RefreshRequestDTO dto) {
        return authService.refresh(dto);
    }
}
