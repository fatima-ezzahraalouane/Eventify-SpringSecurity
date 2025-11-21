package com.eventify.springsecurity.controller;

import com.eventify.springsecurity.dto.RegistrationResponseDTO;
import com.eventify.springsecurity.dto.UserResponseDTO;
import com.eventify.springsecurity.service.RegistrationService;
import com.eventify.springsecurity.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final RegistrationService registrationService;

    public UserController(UserService userService, RegistrationService registrationService) {
        this.userService = userService;
        this.registrationService = registrationService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getProfile(Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        UserResponseDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/events/{id}/register")
    public ResponseEntity<RegistrationResponseDTO> registerToEvent(
            @PathVariable Long id,
            Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        UserResponseDTO user = userService.getUserByEmail(email);
        
        RegistrationResponseDTO registration = registrationService.registerToEvent(user.getId(), id);
        return new ResponseEntity<>(registration, HttpStatus.CREATED);
    }

    
}

