package com.zup.desafio_imposto.services.impl;

import com.zup.desafio_imposto.dtos.request.LoginRequestDTO;
import com.zup.desafio_imposto.dtos.request.UserRequestDTO;
import com.zup.desafio_imposto.dtos.response.LoginResponseDTO;
import com.zup.desafio_imposto.dtos.response.UserResponseDTO;
import com.zup.desafio_imposto.exceptions.DuplicateUsernameException;
import com.zup.desafio_imposto.mappers.UserMapper;
import com.zup.desafio_imposto.models.Role;
import com.zup.desafio_imposto.models.User;
import com.zup.desafio_imposto.repositories.RoleRepository;
import com.zup.desafio_imposto.repositories.UserRepository;
import com.zup.desafio_imposto.services.AuthenticationService;
import com.zup.desafio_imposto.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder bCryptPasswordEncoder;

    private final UserMapper userMapper;

    private final AuthenticationService authenticationService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder bCryptPasswordEncoder, UserMapper userMapper, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
        this.authenticationService = authenticationService;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        validateDuplicateUsername(userRequestDTO.username());
        Set<Role> roles = getRolesFromRequest(userRequestDTO.role());
        String encodedPassword = bCryptPasswordEncoder.encode(userRequestDTO.password());
        User user = userMapper.toEntity(userRequestDTO, encodedPassword, roles);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    private void validateDuplicateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException("Usuário já cadastrado no sistema");
        }
    }

    @Override
    public LoginResponseDTO loginUser(LoginRequestDTO login) {
        return authenticationService.authenticate(login);
    }

    private Set<Role> getRolesFromRequest(Set<String> roleNames) {
        return roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(new Role())))
                .collect(Collectors.toSet());
    }

}
