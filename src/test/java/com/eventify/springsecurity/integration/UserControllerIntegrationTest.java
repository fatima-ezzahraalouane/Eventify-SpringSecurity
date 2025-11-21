package com.eventify.springsecurity.integration;

import com.eventify.springsecurity.entity.Event;
import com.eventify.springsecurity.entity.User;
import com.eventify.springsecurity.enums.Role;
import com.eventify.springsecurity.enums.RegistrationStatus;
import com.eventify.springsecurity.repository.EventRepository;
import com.eventify.springsecurity.repository.RegistrationRepository;
import com.eventify.springsecurity.repository.UserRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;
    @Autowired EventRepository eventRepository;
    @Autowired RegistrationRepository registrationRepository;

    private User user;
    private Event event;

    @BeforeEach
    void setUp() {
        registrationRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();

        User organizer = saveUser("Organizer", "org@test.com", Role.ROLE_ORGANIZER);
        user = saveUser("User", "user@test.com", Role.ROLE_USER);

        event = Event.builder()
                .title("DevFest")
                .description("Event desc")
                .location("Lyon")
                .dateTime(LocalDateTime.now().plusDays(2))
                .capacity(30)
                .organizer(organizer)
                .build();
        eventRepository.save(event);
    }

    @AfterEach
    void tearDown() {
        registrationRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void profile_shouldReturnCurrentUser() throws Exception {
        mockMvc.perform(get("/api/user/profile")
                        .with(httpBasic(user.getEmail(), "pwd")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void registerToEvent_shouldReturn201() throws Exception {
        mockMvc.perform(post("/api/user/events/" + event.getId() + "/register")
                        .with(httpBasic(user.getEmail(), "pwd")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(RegistrationStatus.REGISTERED.name()));
    }

    @Test
    void registrations_shouldReturnList() throws Exception {
        mockMvc.perform(post("/api/user/events/" + event.getId() + "/register")
                        .with(httpBasic(user.getEmail(), "pwd")))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/user/registrations")
                        .with(httpBasic(user.getEmail(), "pwd")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventId").value(event.getId()));
    }

    private User saveUser(String name, String email, Role role) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword("pwd"); // NoOpPasswordEncoder sous profil test
        u.setRole(role);
        return userRepository.save(u);
    }
}