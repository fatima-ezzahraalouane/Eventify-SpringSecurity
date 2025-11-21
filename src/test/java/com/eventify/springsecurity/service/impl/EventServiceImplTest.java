package com.eventify.springsecurity.service;

import com.eventify.springsecurity.dto.EventCreateDTO;
import com.eventify.springsecurity.dto.EventResponseDTO;
import com.eventify.springsecurity.dto.EventUpdateDTO;
import com.eventify.springsecurity.entity.Event;
import com.eventify.springsecurity.entity.User;
import com.eventify.springsecurity.enums.Role;
import com.eventify.springsecurity.mapper.EventMapper;
import com.eventify.springsecurity.repository.EventRepository;
import com.eventify.springsecurity.repository.UserRepository;
import com.eventify.springsecurity.service.impl.EventServiceImpl;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock EventRepository eventRepository;
    @Mock UserRepository userRepository;
    @Mock EventMapper eventMapper;

    @InjectMocks EventServiceImpl eventService;

    private Event event;
    private EventResponseDTO responseDTO;
    private EventCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        User organizer = new User(2L, "Organisateur", "org@mail.com", "pwd", Role.ROLE_ORGANIZER, null, null);
        event = new Event();
        event.setId(1L);
        event.setOrganizer(organizer);
        event.setTitle("Event");

        responseDTO = new EventResponseDTO(1L, "Event", null, null, null, 50, 2L, "Organisateur", 0);
        createDTO = new EventCreateDTO("Event", "desc", "Paris", LocalDateTime.now(), 50);
    }

    @Test
    void createEvent_ok() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(event.getOrganizer()));
        when(eventMapper.toEntity(createDTO)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toDto(event)).thenReturn(responseDTO);

        EventResponseDTO dto = eventService.createEvent(createDTO, 2L);

        assertThat(dto.getTitle()).isEqualTo("Event");
        verify(eventRepository).save(event);
    }

    @Test
    void createEvent_organizerNotFound() {
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.createEvent(createDTO, 5L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void updateEvent_notOrganizer_throws() {
        EventUpdateDTO updateDTO = new EventUpdateDTO("New", "desc", "Paris", LocalDateTime.now(), 20);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.existsByIdAndOrganizerId(1L, 9L)).thenReturn(false);

        assertThatThrownBy(() -> eventService.updateEvent(1L, updateDTO, 9L))
                .isInstanceOf(UnauthorizedActionException.class);
    }

    @Test
    void getEventById_notFound() {
        when(eventRepository.findById(44L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.getEventById(44L))
                .isInstanceOf(EventNotFoundException.class);
    }

        @Test
    void getAllEvents_shouldReturnDtos() {
        when(eventRepository.findAll()).thenReturn(List.of(event));
        when(eventMapper.toDto(event)).thenReturn(responseDTO);

        List<EventResponseDTO> events = eventService.getAllEvents();

        assertThat(events).hasSize(1);
    }

    @Test
    void getEventById_success() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventMapper.toDto(event)).thenReturn(responseDTO);

        EventResponseDTO dto = eventService.getEventById(1L);

        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    void updateEvent_success() {
        EventUpdateDTO dto = new EventUpdateDTO("New","desc","Paris", LocalDateTime.now(), 40);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.existsByIdAndOrganizerId(1L, event.getOrganizer().getId())).thenReturn(true);
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toDto(event)).thenReturn(responseDTO);

        EventResponseDTO result = eventService.updateEvent(1L, dto, event.getOrganizer().getId());

        assertThat(result.getId()).isEqualTo(1L);
        verify(eventMapper).updateEntityFromDto(dto, event);
    }

    @Test
    void deleteEvent_success() {
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.existsByIdAndOrganizerId(1L, event.getOrganizer().getId())).thenReturn(true);

        eventService.deleteEvent(1L, event.getOrganizer().getId());

        verify(eventRepository).deleteById(1L);
    }

    @Test
    void deleteEventByAdmin_success() {
        when(eventRepository.existsById(1L)).thenReturn(true);

        eventService.deleteEventByAdmin(1L);

        verify(eventRepository).deleteById(1L);
    }

    @Test
    void deleteEvent_notFound_shouldThrow() {
        when(eventRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> eventService.deleteEvent(1L, event.getOrganizer().getId()))
                .isInstanceOf(EventNotFoundException.class);
    }
}