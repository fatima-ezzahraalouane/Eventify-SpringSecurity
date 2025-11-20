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

    
}