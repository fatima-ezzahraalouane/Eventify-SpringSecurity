package com.eventify.springsecurity.controller;

import com.eventify.springsecurity.dto.ChangeRoleRequest;
import com.eventify.springsecurity.dto.UserResponseDTO;
import com.eventify.springsecurity.service.EventService;
import com.eventify.springsecurity.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final EventService eventService;

    public AdminController(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<UserResponseDTO> changeUserRole(
            @PathVariable Long id,
            @Valid @RequestBody ChangeRoleRequest request) {
        UserResponseDTO updatedUser = userService.changeUserRole(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEventByAdmin(id);
        return ResponseEntity.noContent().build();
    }
}

