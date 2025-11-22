package com.eventify.springsecurity.mapper;

import com.eventify.springsecurity.dto.RegistrationResponseDTO;
import com.eventify.springsecurity.entity.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {

    @Mapping(target = "userId", expression = "java(registration.getUser().getId())")
    @Mapping(target = "userName", expression = "java(registration.getUser().getName())")
    @Mapping(target = "eventId", expression = "java(registration.getEvent().getId())")
    @Mapping(target = "eventTitle", expression = "java(registration.getEvent().getTitle())")
    @Mapping(target = "status", expression = "java(registration.getStatus().name())")
    RegistrationResponseDTO toDto(Registration registration);
}

