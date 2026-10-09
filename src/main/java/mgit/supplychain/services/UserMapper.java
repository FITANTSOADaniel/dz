package mgit.supplychain.services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import mgit.supplychain.dto.CreateUserDTO;
import mgit.supplychain.dto.ResponseUserDTO;
import mgit.supplychain.entity.User;

@ApplicationScoped
public class UserMapper {

    public ResponseUserDTO toDTO(User user) {
        ResponseUserDTO dto = new ResponseUserDTO();

        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());

        return dto;
    }

    public List<ResponseUserDTO> toDTOs(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .toList();
    }

    public void updateEntity(User user, CreateUserDTO createUserDTO){
        user.setEmail(createUserDTO.getEmail());
        user.setFirstName(createUserDTO.getFirstName());
        user.setName(createUserDTO.getName());
        user.setPhone(createUserDTO.getPhone());
        user.setRole(createUserDTO.getRole());
        if (createUserDTO.getPassword() != null && !createUserDTO.getPassword().isBlank()) { 
            user.setPasswordHash(createUserDTO.getPassword()); 
        }
        
    }

    public User toEntity(CreateUserDTO createUserDTO){ 
        User user = new User();
        user.setEmail(createUserDTO.getEmail()); 
        user.setFirstName(createUserDTO.getFirstName()); 
        user.setName(createUserDTO.getName()); 
        user.setPasswordHash(createUserDTO.getPassword()); 
        user.setPhone(createUserDTO.getPhone());
        user.setRole(createUserDTO.getRole());
        return user; 
    }
}
