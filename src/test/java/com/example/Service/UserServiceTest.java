package com.example.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dto.UserDTO;
import com.example.models.Entity.User;
import com.example.repository.User.UserRepository;
import com.example.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock 
    private UserRepository userRepository;

    @InjectMocks 
    private UserService userService;

    @Test
    void shouldCreateUser(){
        //Arrange
        UserDTO userDTO = new UserDTO("Fitantsoa", "Daniel", "0123456789", new BigDecimal(1));
        User user = new User("Fitantsoa", "Daniel", "0123456789");

        when(userRepository.save(any(User.class))).thenReturn(user);

        //Act
        User result = userService.createUser(userDTO);

        //Assert
        assertEquals("Fitantsoa", result.getName());
        assertEquals("Daniel", result.getFirstName());
        assertEquals("0123456789", result.getPhone());

        verify(userRepository).save(any(User.class));
    }
    @Test
    void shouldGetAll(){
        //Arrange
        User user1 = new User("Fitantsoa", "Daniel", "0123456789");
        User user2 = new User("Jhon", "Doe", "9876543210");

        when(userRepository.getAll()).thenReturn(List.of(user1, user2));

        //Act
        List<User> result = userService.getAll();
 
        //Assert
        assertEquals(2, result.size());
        assertEquals("Daniel", result.get(0).getFirstName());
        assertEquals("Doe", result.get(1).getFirstName());

        verify(userRepository).getAll();
    }

    @Test
    void shouldUpdateUser(){
        // Arrange
        UserDTO userDTO = new UserDTO("Fitantsoa", "Daniel", "0123456789", new BigDecimal(1));
        User updatedUser = new User(1, "Fitantsoa", "Daniel", "0123456789");

        when(userRepository.updateUser(any(User.class), eq("1"))).thenReturn(updatedUser);

        //Act
        User result = userService.updateUser(userDTO, "1");
        
        //Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Fitantsoa", result.getName());
        assertEquals("Daniel", result.getFirstName());
        assertEquals("0123456789", result.getPhone());

         // Assert : ce que le service a envoyé au repository
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).updateUser(captor.capture(), eq("1"));

        User userSent = captor.getValue();

        assertEquals("Fitantsoa", userSent.getName());
        assertEquals("Daniel", userSent.getFirstName());
        assertEquals("0123456789", userSent.getPhone());
    }

    @Test
    void shouldDeleteUser(){
        userService.deleteUser("1");

        verify(userRepository).deleteUser("1");
    }
}
