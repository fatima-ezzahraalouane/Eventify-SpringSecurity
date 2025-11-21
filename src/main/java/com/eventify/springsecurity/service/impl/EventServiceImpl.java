package com.eventify.springsecurity.service.impl;

import com.eventify.springsecurity.dto.EventCreateDTO;
import com.eventify.springsecurity.dto.EventResponseDTO;
import com.eventify.springsecurity.dto.EventUpdateDTO;
import com.eventify.springsecurity.entity.Event;
import com.eventify.springsecurity.entity.User;
import com.eventify.springsecurity.exception.EventNotFoundException;
import com.eventify.springsecurity.exception.UnauthorizedActionException;
import com.eventify.springsecurity.exception.UserNotFoundException;
import com.eventify.springsecurity.mapper.EventMapper;
import com.eventify.springsecurity.repository.EventRepository;
import com.eventify.springsecurity.repository.UserRepository;
import com.eventify.springsecurity.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    public EventServiceImpl(EventRepository eventRepository,
                           UserRepository userRepository,
                           EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public List<EventResponseDTO> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(eventMapper::toDto)
                .toList();
    }

    @Override
    public EventResponseDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Événement non trouvé avec l'ID : " + id));
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    public EventResponseDTO createEvent(EventCreateDTO dto, Long organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException("Organisateur non trouvé avec l'ID : " + organizerId));

        Event event = eventMapper.toEntity(dto);
        event.setOrganizer(organizer);
        
        Event saved = eventRepository.save(event);
        return eventMapper.toDto(saved);
    }

    @Override
    @Transactional
    public EventResponseDTO updateEvent(Long id, EventUpdateDTO dto, Long organizerId) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Événement non trouvé avec l'ID : " + id));

        if (!eventRepository.existsByIdAndOrganizerId(id, organizerId)) {
            throw new UnauthorizedActionException("Vous n'êtes pas autorisé à modifier cet événement");
        }

        eventMapper.updateEntityFromDto(dto, event);
        Event updated = eventRepository.save(event);
        return eventMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteEvent(Long id, Long organizerId) {
        if (!eventRepository.existsById(id)) {
            throw new EventNotFoundException("Événement non trouvé avec l'ID : " + id);
        }

        if (!eventRepository.existsByIdAndOrganizerId(id, organizerId)) {
            throw new UnauthorizedActionException("Vous n'êtes pas autorisé à supprimer cet événement");
        }

        eventRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteEventByAdmin(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new EventNotFoundException("Événement non trouvé avec l'ID : " + id);
        }
        eventRepository.deleteById(id);
    }
}

