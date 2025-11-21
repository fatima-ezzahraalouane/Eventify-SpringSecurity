package com.eventify.springsecurity.service;

import com.eventify.springsecurity.dto.ChangeRoleRequest;
import com.eventify.springsecurity.dto.UserCreateDTO;
import com.eventify.springsecurity.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserCreateDTO dto);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(Long id, UserCreateDTO dto);

    void deleteUser(Long id);

    UserResponseDTO changeUserRole(Long userId, ChangeRoleRequest request);

    UserResponseDTO getUserByEmail(String email);
}