package com.eventify.springsecurity.service;

import com.eventify.springsecurity.dto.EventCreateDTO;
import com.eventify.springsecurity.dto.EventResponseDTO;
import com.eventify.springsecurity.dto.EventUpdateDTO;

import java.util.List;

public interface EventService {

    List<EventResponseDTO> getAllEvents();

    EventResponseDTO getEventById(Long id);

    EventResponseDTO createEvent(EventCreateDTO dto, Long organizerId);

    EventResponseDTO updateEvent(Long id, EventUpdateDTO dto, Long organizerId);

    void deleteEvent(Long id, Long organizerId);

    void deleteEventByAdmin(Long id);
}

