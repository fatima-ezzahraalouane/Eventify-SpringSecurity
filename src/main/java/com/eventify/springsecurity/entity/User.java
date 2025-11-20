package com.eventify.springsecurity.entity;

import com.eventify.springsecurity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est requis")
    @Column(nullable = false)
    private String name;

    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est requis")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "Le rôle est requis")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Relations bidirectionnelles
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> organizedEvents = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Registration> registrations = new ArrayList<>();
}