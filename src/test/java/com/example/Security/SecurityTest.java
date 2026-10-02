package com.example.Security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.security.config.PasswordHasher;

public class SecurityTest {

    @Test
    void shouldHashPassword() {

        PasswordHasher hasher = new PasswordHasher();

        String password = "azerty123";

        String hash = hasher.hash(password);

        assertNotNull(hash);
        assertNotEquals(password, hash);
    }

    @Test
    void shouldVerifyCorrectPassword() {

        PasswordHasher hasher = new PasswordHasher();

        String password = "azerty123";

        String hash = hasher.hash(password);

        assertTrue(hasher.verify(password, hash));
    }

    @Test
    void shouldRejectWrongPassword() {

        PasswordHasher hasher = new PasswordHasher();

        String hash = hasher.hash("azerty123");

        assertFalse(hasher.verify("mauvaisPassword", hash));
    }
}
