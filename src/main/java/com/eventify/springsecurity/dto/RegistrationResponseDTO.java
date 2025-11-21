package com.eventify.springsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponseDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long eventId;
    private String eventTitle;
    private LocalDateTime registeredAt;
    private String status;
}

