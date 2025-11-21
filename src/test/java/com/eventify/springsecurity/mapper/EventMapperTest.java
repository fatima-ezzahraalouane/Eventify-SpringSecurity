package com.eventify.springsecurity.mapper;

import com.eventify.springsecurity.dto.EventCreateDTO;
import com.eventify.springsecurity.dto.EventResponseDTO;
import com.eventify.springsecurity.dto.EventUpdateDTO;
import com.eventify.springsecurity.entity.Event;
import com.eventify.springsecurity.entity.Registration;
import com.eventify.springsecurity.entity.User;
import com.eventify.springsecurity.enums.RegistrationStatus;
import com.eventify.springsecurity.enums.Role;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EventMapperTest {

    private final EventMapper mapper = Mappers.getMapper(EventMapper.class);

    @Test
    void toEntity_setsFieldsAndIgnoresRelations() {
        EventCreateDTO dto = new EventCreateDTO("Title","desc","Paris", LocalDateTime.now(), 50);

        Event entity = mapper.toEntity(dto);

        assertThat(entity.getTitle()).isEqualTo("Title");
        assertThat(entity.getOrganizer()).isNull();
        assertThat(entity.getRegistrations()).isNullOrEmpty();    }

    @Test
    void updateEntity_keepsOrganizer() {
        User organizer = new User(1L,"Org","org@mail.com","pwd", Role.ROLE_ORGANIZER,null,null);
        Event event = Event.builder().id(5L).organizer(organizer).title("Old").build();
        EventUpdateDTO dto = new EventUpdateDTO("New","desc","Nice", LocalDateTime.now(), 40);

        mapper.updateEntityFromDto(dto, event);

        assertThat(event.getTitle()).isEqualTo("New");
        assertThat(event.getOrganizer()).isSameAs(organizer);
    }

    @Test
    void toDto_computesCurrentRegistrations() {
        User organizer = new User(1L,"Org","org@mail.com","pwd", Role.ROLE_ORGANIZER,null,null);
        Event event = Event.builder()
                .id(7L)
                .title("Conf")
                .organizer(organizer)
                .registrations(List.of(
                        Registration.builder().status(RegistrationStatus.REGISTERED).build(),
                        Registration.builder().status(RegistrationStatus.CANCELLED).build()
                ))
                .build();

        EventResponseDTO dto = mapper.toDto(event);

        assertThat(dto.getOrganizerId()).isEqualTo(1L);
        assertThat(dto.getOrganizerName()).isEqualTo("Org");
        assertThat(dto.getCurrentRegistrations()).isEqualTo(1);
    }
}