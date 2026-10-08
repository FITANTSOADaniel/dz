package mgit.supplychain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class CreateUserDTO {
    private String name;
    private String firstName;
    private String email;
    private String phone;
    private String password;
}
