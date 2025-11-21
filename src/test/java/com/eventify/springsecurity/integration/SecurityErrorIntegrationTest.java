package com.eventify.springsecurity.integration;

import com.eventify.springsecurity.entity.User;
import com.eventify.springsecurity.enums.Role;
import com.eventify.springsecurity.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityErrorIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        user = new User();
        user.setName("User");
        user.setEmail("user@test.com");
        user.setPassword("pwd");
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void accessUserProfile_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/user/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accessAdminRoute_withUserRole_returns403() throws Exception {
        mockMvc.perform(get("/api/admin/users")
                        .with(httpBasic(user.getEmail(), "pwd")))
                .andExpect(status().isForbidden());
    }
}