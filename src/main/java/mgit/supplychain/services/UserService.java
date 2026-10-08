package mgit.supplychain.services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mgit.supplychain.repository.UserRepository;
import mgit.supplychain.dto.ResponseUserDTO;
import mgit.supplychain.entity.User;

@ApplicationScoped
public class UserService {
    @Inject
    UserMapper userMapper;

    @Inject 
    UserRepository userRepository;

    public List<ResponseUserDTO> findAll(){
        List<User> users = userRepository.listAll();
        return userMapper.toDTOs(users);
    } 
}
