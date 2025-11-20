package com.eventify.springsecurity.service.impl;

import com.eventify.springsecurity.dto.ChangeRoleRequest;
import com.eventify.springsecurity.dto.UserCreateDTO;
import com.eventify.springsecurity.dto.UserResponseDTO;
import com.eventify.springsecurity.entity.User;
import com.eventify.springsecurity.enums.Role;
import com.eventify.springsecurity.exception.InvalidRoleException;
import com.eventify.springsecurity.exception.UserNotFoundException;
import com.eventify.springsecurity.exception.UsernameAlreadyExistsException;
import com.eventify.springsecurity.mapper.UserMapper;
import com.eventify.springsecurity.repository.UserRepository;
import com.eventify.springsecurity.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UsernameAlreadyExistsException("Un utilisateur avec cet email existe déjà");
        }
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID : " + id));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserCreateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID : " + id));

        // verifier si l'email existe deja pour un autre utilisateur
        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new UsernameAlreadyExistsException("Un utilisateur avec cet email existe déjà");
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User updated = userRepository.save(user);
        return userMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Utilisateur non trouvé avec l'ID : " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UserResponseDTO changeUserRole(Long userId, ChangeRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));

        try {
            Role newRole = Role.valueOf(request.getRole().toUpperCase());
            // verifier que le role est valide (commence par ROLE_)
            if (!newRole.name().startsWith("ROLE_")) {
                throw new InvalidRoleException("Le rôle doit être au format ROLE_XXX");
            }
            user.setRole(newRole);
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Rôle invalide : " + request.getRole() + ". Les rôles valides sont : ROLE_USER, ROLE_ADMIN, ROLE_ORGANIZER");
        }

        User updated = userRepository.save(user);
        return userMapper.toDto(updated);
    }
}