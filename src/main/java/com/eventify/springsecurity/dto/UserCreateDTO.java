package com.eventify.springsecurity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {
    
    @NotBlank(message = "Le nom est requis")
    private String name;
    
    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est requis")
    private String email;
    
    @NotBlank(message = "Le mot de passe est requis")
    private String password;
}