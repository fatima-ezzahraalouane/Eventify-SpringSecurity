package com.eventify.springsecurity.mapper;

import com.eventify.springsecurity.dto.UserCreateDTO;
import com.eventify.springsecurity.dto.UserResponseDTO;
import com.eventify.springsecurity.entity.User;
import com.eventify.springsecurity.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(defaultRole())")
    User toEntity(UserCreateDTO dto);

    UserResponseDTO toDto(User user);

    default Role defaultRole() {
        return Role.ROLE_USER;
    }
}