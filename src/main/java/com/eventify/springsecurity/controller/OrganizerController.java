package com.eventify.springsecurity.controller;

import com.eventify.springsecurity.dto.EventCreateDTO;
import com.eventify.springsecurity.dto.EventResponseDTO;
import com.eventify.springsecurity.dto.EventUpdateDTO;
import com.eventify.springsecurity.dto.UserResponseDTO;
import com.eventify.springsecurity.service.EventService;
import com.eventify.springsecurity.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizer")
public class OrganizerController {

    private final EventService eventService;
    private final UserService userService;

    public OrganizerController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @PostMapping("/events")
    public ResponseEntity<EventResponseDTO> createEvent(
            @Valid @RequestBody EventCreateDTO dto,
            Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        UserResponseDTO user = userService.getUserByEmail(email);
        
        EventResponseDTO createdEvent = eventService.createEvent(dto, user.getId());
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventUpdateDTO dto,
            Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        UserResponseDTO user = userService.getUserByEmail(email);
        
        EventResponseDTO updatedEvent = eventService.updateEvent(id, dto, user.getId());
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id,
            Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        UserResponseDTO user = userService.getUserByEmail(email);
        
        eventService.deleteEvent(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}

