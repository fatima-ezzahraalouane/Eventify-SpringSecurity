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

    @Override
    @Transactional
    public RegistrationResponseDTO registerToEvent(Long userId, Long eventId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Événement non trouvé avec l'ID : " + eventId));

        if (registrationRepository.existsByUserIdAndEventId(userId, eventId)) {
            Registration existingRegistration = registrationRepository.findByUserIdAndEventId(userId, eventId)
                    .orElseThrow();
            
            if (existingRegistration.getStatus() == RegistrationStatus.CANCELLED) {
                existingRegistration.setStatus(RegistrationStatus.REGISTERED);
                Registration updated = registrationRepository.save(existingRegistration);
                return registrationMapper.toDto(updated);
            }
            
            throw new UnauthorizedActionException("Vous êtes déjà inscrit à cet événement");
        }

        long currentRegistrations = registrationRepository.countByEventIdAndStatus(
                eventId, RegistrationStatus.REGISTERED);
        
        if (currentRegistrations >= event.getCapacity()) {
            throw new UnauthorizedActionException("L'événement est complet. Capacité maximale : " + event.getCapacity());
        }

        Registration registration = Registration.builder()
                .user(user)
                .event(event)
                .status(RegistrationStatus.REGISTERED)
                .build();

        Registration saved = registrationRepository.save(registration);
        return registrationMapper.toDto(saved);
    }

    @Override
    public List<RegistrationResponseDTO> getUserRegistrations(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Utilisateur non trouvé avec l'ID : " + userId);
        }

        return registrationRepository.findByUserId(userId)
                .stream()
                .map(registrationMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public RegistrationResponseDTO cancelRegistration(Long userId, Long eventId) {
        Registration registration = registrationRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new EventNotFoundException("Inscription non trouvée pour cet utilisateur et cet événement"));

        if (!registration.getUser().getId().equals(userId)) {
            throw new UnauthorizedActionException("Vous n'êtes pas autorisé à annuler cette inscription");
        }

        registration.setStatus(RegistrationStatus.CANCELLED);
        Registration updated = registrationRepository.save(registration);
        return registrationMapper.toDto(updated);
    }
}

