package com.eventify.springsecurity.service.impl;

import com.eventify.springsecurity.dto.RegistrationResponseDTO;
import com.eventify.springsecurity.entity.Event;
import com.eventify.springsecurity.entity.Registration;
import com.eventify.springsecurity.entity.User;
import com.eventify.springsecurity.enums.RegistrationStatus;
import com.eventify.springsecurity.exception.EventNotFoundException;
import com.eventify.springsecurity.exception.UnauthorizedActionException;
import com.eventify.springsecurity.exception.UserNotFoundException;
import com.eventify.springsecurity.mapper.RegistrationMapper;
import com.eventify.springsecurity.repository.EventRepository;
import com.eventify.springsecurity.repository.RegistrationRepository;
import com.eventify.springsecurity.repository.UserRepository;
import com.eventify.springsecurity.service.RegistrationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RegistrationMapper registrationMapper;

    public RegistrationServiceImpl(RegistrationRepository registrationRepository,
                                  EventRepository eventRepository,
                                  UserRepository userRepository,
                                  RegistrationMapper registrationMapper) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.registrationMapper = registrationMapper;
    }

    
}

