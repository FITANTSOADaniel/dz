package com.example.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;

import com.example.Controller.UserController.UserController;
import com.example.models.Entity.User;
import com.example.services.UserService;

import jakarta.ws.rs.core.Response;

class UserControllerHttpTest extends JerseyTest {

    private UserService userService = mock(UserService.class);

    @Override
    protected ResourceConfig configure() {
        return new ResourceConfig()
            .register(UserController.class)
            .register(JacksonFeature.class)
            .register(new AbstractBinder() {

                @Override
                protected void configure() {
                    bind(userService)
                        .to(UserService.class);
                }
            });
    }

    @Test
    void shouldGetAllUsers() {
        User user1 = new User(
                "Rakoto",
                "Daniel",
                "0340000000"
        );

        User user2 = new User(
                "Rabe",
                "Jean",
                "0320000000"
        );

        when(userService.getAll())
                .thenReturn(List.of(user1, user2));

        Response response = target("/users")
                .request()
                .get();

        assertEquals(200, response.getStatus());

        verify(userService).getAll();
    }

    @Test
    void shouldGetUserById() {

        User user = new User(
                1,
                "Rakoto",
                "Daniel",
                "0340000000"
        );

        when(userService.findById(1))
                .thenReturn(Optional.of(user));

        Response response = target("/users/1")
                .request()
                .get();

        assertEquals(200, response.getStatus());

        verify(userService).findById(1);
    }

    @Test
    void shouldReturn404WhenUserDoesNotExist() {

        when(userService.findById(999))
                .thenReturn(Optional.empty());

        Response response = target("/users/999")
                .request()
                .get();

        assertEquals(404, response.getStatus());

        verify(userService).findById(999);
    }
}