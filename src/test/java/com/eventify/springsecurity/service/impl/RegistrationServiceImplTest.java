// java
package com.eventify.springsecurity.service.impl;

import com.eventify.springsecurity.dto.RegistrationResponseDTO;
import com.eventify.springsecurity.entity.Event;
import com.eventify.springsecurity.entity.Registration;
import com.eventify.springsecurity.entity.User;
import com.eventify.springsecurity.enums.RegistrationStatus;
import com.eventify.springsecurity.enums.Role;
import com.eventify.springsecurity.mapper.RegistrationMapper;
import com.eventify.springsecurity.repository.EventRepository;
import com.eventify.springsecurity.repository.RegistrationRepository;
import com.eventify.springsecurity.repository.UserRepository;
import com.eventify.springsecurity.exception.UserNotFoundException;
import com.eventify.springsecurity.exception.UnauthorizedActionException;
import com.eventify.springsecurity.exception.EventNotFoundException;
import com.eventify.springsecurity.exception.InvalidRoleException;
import com.eventify.springsecurity.exception.UsernameAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    @Mock RegistrationRepository registrationRepository;
    @Mock EventRepository eventRepository;
    @Mock UserRepository userRepository;
    @Mock RegistrationMapper registrationMapper;

    @InjectMocks RegistrationServiceImpl registrationService;

    private User user;
    private Event event;
    private Registration registration;
    private RegistrationResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        user = new User(1L, "User", "user@mail.com", "pwd", Role.ROLE_USER, null, null);
        event = new Event();
        event.setId(10L);
        event.setCapacity(2);
        registration = new Registration(5L, user, event, null, RegistrationStatus.REGISTERED);
        responseDTO = new RegistrationResponseDTO(5L, 1L, "User", 10L, "Event Name", null, "REGISTERED");
    }


    @Test
    void registerToEvent_ok() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));
        when(registrationRepository.existsByUserIdAndEventId(1L, 10L)).thenReturn(false);
        when(registrationRepository.countByEventIdAndStatus(10L, RegistrationStatus.REGISTERED)).thenReturn(1L);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);
        when(registrationMapper.toDto(registration)).thenReturn(responseDTO);

        RegistrationResponseDTO dto = registrationService.registerToEvent(1L, 10L);

        assertThat(dto.getEventId()).isEqualTo(10L);
    }

    @Test
    void registerToEvent_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> registrationService.registerToEvent(1L, 10L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void registerToEvent_eventFull() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));
        when(registrationRepository.existsByUserIdAndEventId(1L, 10L)).thenReturn(false);
        when(registrationRepository.countByEventIdAndStatus(10L, RegistrationStatus.REGISTERED)).thenReturn(2L);

        assertThatThrownBy(() -> registrationService.registerToEvent(1L, 10L))
                .isInstanceOf(UnauthorizedActionException.class);
    }

    @Test
    void cancelRegistration_notFound() {
        when(registrationRepository.findByUserIdAndEventId(1L, 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> registrationService.cancelRegistration(1L, 10L))
                .isInstanceOf(EventNotFoundException.class);
    }

    @Test
    void cancelRegistration_ok() {
        when(registrationRepository.findByUserIdAndEventId(1L, 10L))
                .thenReturn(Optional.of(registration));
        when(registrationRepository.save(registration)).thenReturn(registration);
        when(registrationMapper.toDto(registration)).thenReturn(responseDTO);

        RegistrationResponseDTO dto = registrationService.cancelRegistration(1L, 10L);

        assertThat(dto.getStatus()).isEqualTo("REGISTERED"); // après setStatus -> mapper renvoie REGISTERED
        verify(registrationRepository).save(registration);
    }

    @Test
    void cancelRegistration_wrongUser_shouldThrow() {
        Registration other = Registration.builder()
                .id(6L)
                .user(new User(99L,"Other","other@mail.com","pwd", Role.ROLE_USER,null,null))
                .event(event)
                .status(RegistrationStatus.REGISTERED)
                .build();
        when(registrationRepository.findByUserIdAndEventId(1L, 10L))
                .thenReturn(Optional.of(other));

        assertThatThrownBy(() -> registrationService.cancelRegistration(1L, 10L))
                .isInstanceOf(UnauthorizedActionException.class);
    }

        @Test
    void registerToEvent_existingCancelled_shouldReactivate() {
        Registration cancelled = new Registration(6L, user, event, null, RegistrationStatus.CANCELLED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));
        when(registrationRepository.existsByUserIdAndEventId(1L, 10L)).thenReturn(true);
        when(registrationRepository.findByUserIdAndEventId(1L, 10L)).thenReturn(Optional.of(cancelled));
        when(registrationRepository.save(cancelled)).thenReturn(cancelled);
        when(registrationMapper.toDto(cancelled)).thenReturn(responseDTO);

        RegistrationResponseDTO dto = registrationService.registerToEvent(1L, 10L);

        assertThat(cancelled.getStatus()).isEqualTo(RegistrationStatus.REGISTERED);
        assertThat(dto.getId()).isEqualTo(responseDTO.getId());
    }

    @Test
    void getUserRegistrations_ok() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(registrationRepository.findByUserId(1L)).thenReturn(List.of(registration));
        when(registrationMapper.toDto(registration)).thenReturn(responseDTO);

        List<RegistrationResponseDTO> dtos = registrationService.getUserRegistrations(1L);

        assertThat(dtos).hasSize(1);
    }

    @Test
    void registerToEvent_alreadyRegistered_shouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));
        when(registrationRepository.existsByUserIdAndEventId(1L, 10L)).thenReturn(true);
        when(registrationRepository.findByUserIdAndEventId(1L, 10L)).thenReturn(Optional.of(registration));

        assertThatThrownBy(() -> registrationService.registerToEvent(1L, 10L))
                .isInstanceOf(UnauthorizedActionException.class);
    }
}
