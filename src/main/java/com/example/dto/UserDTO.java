package com.example.dto;

import java.math.BigDecimal;

import com.example.models.RoleUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class UserDTO {
    @NotBlank 
    private String name;
    @Email
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String phone;
    @NotBlank 
    @PositiveOrZero 
    private BigDecimal balance;
    @NotBlank
    private String password;
    private RoleUser role;
}
