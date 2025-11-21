package com.eventify.springsecurity.controller;

import com.eventify.springsecurity.dto.ChangeRoleRequest;
import com.eventify.springsecurity.dto.UserResponseDTO;
import com.eventify.springsecurity.service.EventService;
import com.eventify.springsecurity.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final EventService eventService;

    
}

