package com.eventify.springsecurity.integration;

import com.eventify.springsecurity.dto.UserCreateDTO;
import com.eventify.springsecurity.entity.Event;
import com.eventify.springsecurity.entity.User;
import com.eventify.springsecurity.enums.Role;
import com.eventify.springsecurity.repository.EventRepository;
import com.eventify.springsecurity.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PublicControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepository;
    @Autowired EventRepository eventRepository;

    @AfterEach
    void cleanup() {
        eventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void registerUser_shouldReturn201() throws Exception {
        UserCreateDTO dto = new UserCreateDTO("Alice", "alice@test.com", "pwd");

        mockMvc.perform(post("/api/public/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("alice@test.com"));
    }

    @Test
    void getPublicEvents_shouldReturnList() throws Exception {
        User organizer = saveUser("Organizer", "org@test.com", Role.ROLE_ORGANIZER);
        Event event = Event.builder()
                .title("Conf Spring")
                .description("Intro Spring Security")
                .location("Paris")
                .dateTime(LocalDateTime.now().plusDays(1))
                .capacity(50)
                .organizer(organizer)
                .build();
        eventRepository.save(event);

        mockMvc.perform(get("/api/public/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Conf Spring"));
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