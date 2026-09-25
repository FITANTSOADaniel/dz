package com.example.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.Controller.UserController.UserController;
import com.example.models.RoleUser;
import com.example.models.Entity.User;
import com.example.services.UserService;

public class UserControllerTest {
    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void shouldGetAllUsers() {
        List<User> users = List.of(
            new User(
                "Rakoto",
                "Daniel",
                "daniel@test.com",
                "0340000000",
                RoleUser.CLIENT,
                new BigDecimal("10000.00")
            )
        );
        when(userService.getAll()).thenReturn(users);
        List<User> result = userController.getAll();

        assertEquals(1, result.size());
        assertEquals("Daniel", result.get(0).getFirstName());
        verify(userService).getAll();
    }

    
}
