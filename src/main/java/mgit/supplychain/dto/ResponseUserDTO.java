package mgit.supplychain.dto;

import lombok.Getter;
import lombok.Setter;
import mgit.supplychain.entity.enumeration.RoleUser;

@Getter 
@Setter 
public class ResponseUserDTO {
    private String name;
    private String firstName;
    private String email;
    private String phone;
    private RoleUser role;
}
