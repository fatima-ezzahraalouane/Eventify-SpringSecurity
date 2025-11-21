package com.eventify.springsecurity.mapper;

import com.eventify.springsecurity.dto.RegistrationResponseDTO;
import com.eventify.springsecurity.entity.Event;
import com.eventify.springsecurity.entity.Registration;
import com.eventify.springsecurity.entity.User;
import com.eventify.springsecurity.enums.RegistrationStatus;
import com.eventify.springsecurity.enums.Role;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RegistrationMapperTest {

    private final RegistrationMapper mapper = Mappers.getMapper(RegistrationMapper.class);

    @Test
    void toDto_mapsUserAndEventFields() {
        User user = new User(1L,"User","user@mail.com","pwd", Role.ROLE_USER,null,null);
        Event event = Event.builder().id(10L).title("Conf").build();
        Registration reg = Registration.builder()
                .id(5L)
                .user(user)
                .event(event)
                .status(RegistrationStatus.REGISTERED)
                .registeredAt(LocalDateTime.now())
                .build();

        RegistrationResponseDTO dto = mapper.toDto(reg);

        assertThat(dto.getUserId()).isEqualTo(1L);
        assertThat(dto.getUserName()).isEqualTo("User");
        assertThat(dto.getEventId()).isEqualTo(10L);
        assertThat(dto.getEventTitle()).isEqualTo("Conf");
        assertThat(dto.getStatus()).isEqualTo("REGISTERED");
    }
}