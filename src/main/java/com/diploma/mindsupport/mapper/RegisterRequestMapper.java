package com.diploma.mindsupport.mapper;

import com.diploma.mindsupport.dto.RegisterRequest;
import com.diploma.mindsupport.model.User;
import com.diploma.mindsupport.model.UserRole;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Mapper(componentModel = "spring")
public interface RegisterRequestMapper {

    @AfterMapping
    default void setGrantedAuthorities(@MappingTarget User.UserBuilder user, RegisterRequest registerRequest) {
        user.grantedAuthorities(Collections.singleton(UserRole.valueOf(registerRequest.getUserRoleDto().name())));
    }

    @AfterMapping
    default void encryptPassword(@MappingTarget User.UserBuilder user, PasswordEncoder passwordEncoder, RegisterRequest registerRequest) {
        user.password(passwordEncoder.encode(registerRequest.getPassword()));
    }

    @Mapping(target = "createdAt", expression = "java(java.time.ZonedDateTime.now())")
    User registerRequestToUser(RegisterRequest registerRequest, PasswordEncoder passwordEncoder);
}
