package mgit.supplychain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import mgit.supplychain.entity.enumeration.RoleUser;

@Getter 
@Setter 
public class CreateUserDTO {
    @NotBlank(message="le nom est requis")
    @Size(max=255)
    private String name;

    @NotBlank(message="le prénom est requis")
    @Size(max=255)
    private String firstName;

    @Email
    private String email;

    @NotBlank 
    private String phone;
    
    @NotBlank
    private String password;

    private RoleUser role;
}
