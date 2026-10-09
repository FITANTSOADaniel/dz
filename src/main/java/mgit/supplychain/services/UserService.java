package mgit.supplychain.services;

import java.util.List;
import java.util.Optional;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import mgit.supplychain.repository.UserRepository;
import mgit.supplychain.dto.CreateUserDTO;
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

    public Optional<User> findById(Long id){
        return Optional.ofNullable(userRepository.findById(id));
    }
    
    @Transactional 
    public User createUser(CreateUserDTO createUserDTO){
        User user = userMapper.toEntity(createUserDTO);
        String passwordHash = BcryptUtil.bcryptHash(createUserDTO.getPassword());
        user.setPasswordHash(passwordHash);
        user.setRole(createUserDTO.getRole());
        userRepository.persist(user);
        return user;
    }

    @Transactional
    public Optional<User> updateUser(Long id, CreateUserDTO createUserDTO) { 
        User existUser = userRepository.findById(id);
        if (existUser == null) return Optional.empty();

        userMapper.updateEntity(existUser, createUserDTO);
        String passwordHash = BcryptUtil.bcryptHash(createUserDTO.getPassword());
        existUser.setPasswordHash(passwordHash);
        return Optional.of(existUser); }

    @Transactional
    public boolean delete(Long id){
        return userRepository.deleteById(id);
    }
}
