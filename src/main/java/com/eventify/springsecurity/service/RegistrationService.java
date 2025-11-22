package com.eventify.springsecurity.service;

import com.eventify.springsecurity.dto.RegistrationResponseDTO;

import java.util.List;

public interface RegistrationService {

    RegistrationResponseDTO registerToEvent(Long userId, Long eventId);

    List<RegistrationResponseDTO> getUserRegistrations(Long userId);

    RegistrationResponseDTO cancelRegistration(Long userId, Long eventId);
}

