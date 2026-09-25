package com.example.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
    @NotBlank
    private String firstName;
    @NotBlank
    private String phone;
    @NotBlank 
    @Positive 
    private BigDecimal balance;
}
