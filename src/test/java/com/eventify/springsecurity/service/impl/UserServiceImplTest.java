package com.eventify.springsecurity.service.impl;

import com.eventify.springsecurity.dto.ChangeRoleRequest;
import com.eventify.springsecurity.dto.UserCreateDTO;
import com.eventify.springsecurity.dto.UserResponseDTO;
import com.eventify.springsecurity.entity.User;
import com.eventify.springsecurity.enums.Role;
import com.eventify.springsecurity.mapper.UserMapper;
import com.eventify.springsecurity.repository.UserRepository;
import com.eventify.springsecurity.exception.UserNotFoundException;
import com.eventify.springsecurity.exception.UnauthorizedActionException;
import com.eventify.springsecurity.exception.EventNotFoundException;
import com.eventify.springsecurity.exception.InvalidRoleException;
import com.eventify.springsecurity.exception.UsernameAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock UserRepository userRepository;
    @Mock UserMapper userMapper;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks UserServiceImpl userService;

    private User entity;
    private UserResponseDTO responseDTO;
    private UserCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        entity = new User(1L, "Alice", "alice@mail.com", "encoded", Role.ROLE_USER, null, null);
        responseDTO = new UserResponseDTO(1L, "Alice", "alice@mail.com", "ROLE_USER");
        createDTO = new UserCreateDTO("Alice", "alice@mail.com", "123456");
    }

    @Test
    void createUser_ok() {
        when(userRepository.existsByEmail(createDTO.getEmail())).thenReturn(false);
        when(userMapper.toEntity(createDTO)).thenReturn(entity);
        when(passwordEncoder.encode(createDTO.getPassword())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(entity);
        when(userMapper.toDto(entity)).thenReturn(responseDTO);

        UserResponseDTO result = userService.createUser(createDTO);

        assertThat(result.getEmail()).isEqualTo("alice@mail.com");
        verify(userRepository).save(entity);
    }

    @Test
    void createUser_duplicateEmail_throws() {
        when(userRepository.existsByEmail(createDTO.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(createDTO))
                .isInstanceOf(UsernameAlreadyExistsException.class);
    }

    @Test
    void getUserById_ok() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userMapper.toDto(entity)).thenReturn(responseDTO);

        UserResponseDTO dto = userService.getUserById(1L);

        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void changeUserRole_ok() {
        ChangeRoleRequest request = new ChangeRoleRequest("ROLE_ADMIN");
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userRepository.save(entity)).thenReturn(entity);
        when(userMapper.toDto(entity)).thenReturn(responseDTO);

        userService.changeUserRole(1L, request);

        assertThat(entity.getRole()).isEqualTo(Role.ROLE_ADMIN);
    }

    @Test
    void changeUserRole_invalidRole() {
        ChangeRoleRequest request = new ChangeRoleRequest("BAD_ROLE");
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> userService.changeUserRole(1L, request))
                .isInstanceOf(InvalidRoleException.class);
    }

        @Test
    void getAllUsers_shouldReturnDtoList() {
        when(userRepository.findAll()).thenReturn(List.of(entity));
        when(userMapper.toDto(entity)).thenReturn(responseDTO);

        List<UserResponseDTO> users = userService.getAllUsers();

        assertThat(users).hasSize(1);
        verify(userRepository).findAll();
    }

    @Test
    void updateUser_shouldEncodePasswordAndReturnDto() {
        UserCreateDTO update = new UserCreateDTO("New", "new@mail.com", "newpwd");
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userRepository.existsByEmail(update.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(update.getPassword())).thenReturn("encoded");
        when(userRepository.save(entity)).thenReturn(entity);
        when(userMapper.toDto(entity)).thenReturn(responseDTO);

        UserResponseDTO dto = userService.updateUser(1L, update);

        assertThat(dto.getId()).isEqualTo(1L);
        verify(passwordEncoder).encode("newpwd");
    }

    @Test
    void updateUser_emailAlreadyUsed_shouldThrow() {
        UserCreateDTO update = new UserCreateDTO("New", "other@mail.com", "pwd");
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userRepository.existsByEmail(update.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(1L, update))
                .isInstanceOf(UsernameAlreadyExistsException.class);
    }

    @Test
    void deleteUser_notFound_shouldThrow() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void deleteUser_shouldCallRepository() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}