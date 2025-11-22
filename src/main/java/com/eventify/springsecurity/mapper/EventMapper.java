package com.eventify.springsecurity.mapper;

import com.eventify.springsecurity.dto.EventCreateDTO;
import com.eventify.springsecurity.dto.EventResponseDTO;
import com.eventify.springsecurity.dto.EventUpdateDTO;
import com.eventify.springsecurity.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "registrations", ignore = true)
    Event toEntity(EventCreateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "registrations", ignore = true)
    void updateEntityFromDto(EventUpdateDTO dto, @MappingTarget Event event);

    @Mapping(target = "organizerId", expression = "java(event.getOrganizer().getId())")
    @Mapping(target = "organizerName", expression = "java(event.getOrganizer().getName())")
    @Mapping(target = "currentRegistrations", expression = "java(event.getRegistrations() != null ? (int) event.getRegistrations().stream().filter(r -> r.getStatus().name().equals(\"REGISTERED\")).count() : 0)")
    EventResponseDTO toDto(Event event);
}

