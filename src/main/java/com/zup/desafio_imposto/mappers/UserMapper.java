package com.zup.desafio_imposto.mappers;

import com.zup.desafio_imposto.dtos.response.UserResponseDTO;
import com.zup.desafio_imposto.models.Role;
import com.zup.desafio_imposto.models.User;
import com.zup.desafio_imposto.dtos.request.UserRequestDTO;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO userRequest, String encodedPassword, Set<Role> roles) {
        User user = new User();
        user.setUsername(userRequest.username());
        user.setPassword(encodedPassword);
        user.setRoles(roles);
        return user;
    }


    public UserResponseDTO toResponse(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        return new UserResponseDTO(user.getId(), user.getUsername(), roles);
    }
}
