package com.eventify.springsecurity.integration;

import com.eventify.springsecurity.dto.ChangeRoleRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepository;
    @Autowired EventRepository eventRepository;

    private User admin;
    private User targetUser;
    private Event event;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        userRepository.deleteAll();

        admin = saveUser("Admin", "admin@test.com", Role.ROLE_ADMIN);
        targetUser = saveUser("Target", "target@test.com", Role.ROLE_USER);

        User organizer = saveUser("Organizer", "org@test.com", Role.ROLE_ORGANIZER);
        event = Event.builder()
                .title("Admin Event")
                .description("Desc")
                .location("Nice")
                .dateTime(LocalDateTime.now().plusDays(5))
                .capacity(10)
                .organizer(organizer)
                .build();
        eventRepository.save(event);
    }

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void listUsers_requiresAdmin() throws Exception {
        mockMvc.perform(get("/api/admin/users")
                        .with(httpBasic(admin.getEmail(), "pwd")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(admin.getEmail()));
    }

    @Test
    void changeUserRole_shouldReturnUpdatedUser() throws Exception {
        ChangeRoleRequest request = new ChangeRoleRequest("ROLE_ORGANIZER");

        mockMvc.perform(put("/api/admin/users/" + targetUser.getId() + "/role")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .with(httpBasic(admin.getEmail(), "pwd")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ROLE_ORGANIZER"));
    }

    @Test
    void deleteEvent_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/admin/events/" + event.getId())
                        .with(httpBasic(admin.getEmail(), "pwd")))
                .andExpect(status().isNoContent());
    }

    private User saveUser(String name, String email, Role role) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword("pwd");
        u.setRole(role);
        return userRepository.save(u);
    }
}
