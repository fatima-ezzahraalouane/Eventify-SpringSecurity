package com.eventify.springsecurity.integration;

import com.eventify.springsecurity.dto.EventCreateDTO;
import com.eventify.springsecurity.dto.EventUpdateDTO;
import com.eventify.springsecurity.entity.Event;
import com.eventify.springsecurity.entity.User;
import com.eventify.springsecurity.enums.Role;
import com.eventify.springsecurity.repository.EventRepository;
import com.eventify.springsecurity.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrganizerControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepository;
    @Autowired EventRepository eventRepository;

    private User organizer;
    private User otherOrganizer;
    private Event existingEvent;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        userRepository.deleteAll();

        organizer = saveUser("Organizer", "org@test.com", Role.ROLE_ORGANIZER);
        otherOrganizer = saveUser("Other", "other@test.com", Role.ROLE_ORGANIZER);

        existingEvent = Event.builder()
                .title("Initial")
                .description("desc")
                .location("Paris")
                .dateTime(LocalDateTime.now().plusDays(3))
                .capacity(20)
                .organizer(organizer)
                .build();
        eventRepository.save(existingEvent);
    }

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createEvent_shouldReturn201() throws Exception {
        EventCreateDTO dto = new EventCreateDTO(
                "New Event",
                "description",
                "Lyon",
                LocalDateTime.now().plusDays(5),
                100
        );

        mockMvc.perform(post("/api/organizer/events")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto))
                        .with(httpBasic(organizer.getEmail(), "pwd")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Event"));
    }

    @Test
    void updateEvent_shouldReturnUpdatedDto() throws Exception {
        EventUpdateDTO dto = new EventUpdateDTO(
                "Updated title",
                "updated desc",
                "Nice",
                LocalDateTime.now().plusDays(7),
                200
        );

        mockMvc.perform(put("/api/organizer/events/" + existingEvent.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto))
                        .with(httpBasic(organizer.getEmail(), "pwd")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated title"));
    }

    @Test
    void deleteEvent_unauthorizedOrganizer_shouldReturn403() throws Exception {
        mockMvc.perform(delete("/api/organizer/events/" + existingEvent.getId())
                        .with(httpBasic(otherOrganizer.getEmail(), "pwd")))
                .andExpect(status().isForbidden());
    }

    private User saveUser(String name, String email, Role role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword("pwd");
        user.setRole(role);
        return userRepository.save(user);
    }
}