package com.zup.desafio_imposto.services.impl;

import com.zup.desafio_imposto.dtos.request.UserRequestDTO;
import com.zup.desafio_imposto.dtos.response.UserResponseDTO;
import com.zup.desafio_imposto.exceptions.DuplicateUsernameException;
import com.zup.desafio_imposto.mappers.UserMapper;
import com.zup.desafio_imposto.models.Role;
import com.zup.desafio_imposto.models.User;
import com.zup.desafio_imposto.repositories.RoleRepository;
import com.zup.desafio_imposto.repositories.UserRepository;
import com.zup.desafio_imposto.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, roleRepository, passwordEncoder, userMapper, authenticationService);
    }

    /*
    * Em vez de verificar se os métodos username() e roles() foram chamados no mock
    * de UserResponseDTO verificar os valores retornados no objeto response diretamente
    * */
    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO("newUser", "password123", Set.of("ROLE_USER"));
        Role role = new Role(1L, "ROLE_USER");
        User user = new User();
        user.setUsername("newUser");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(role));
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("newUser");
        savedUser.setPassword("encodedPassword");
        savedUser.setRoles(Set.of(role));
        UserResponseDTO userResponseDTO = new UserResponseDTO(1L, "newUser", Set.of("ROLE_USER")); // Use um objeto real

        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userMapper.toEntity(userRequestDTO, "encodedPassword", Set.of(role))).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO response = userService.createUser(userRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals("newUser", response.username()); // Verifica o username diretamente
        assertEquals(Set.of("ROLE_USER"), response.role()); // Verifica as roles diretamente
        verify(userRepository, times(1)).existsByUsername("newUser");
        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO("existingUser", "password123", Set.of("ROLE_USER"));
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateUsernameException.class, () -> userService.createUser(userRequestDTO));
        verify(userRepository, times(1)).existsByUsername("existingUser");
        verifyNoInteractions(roleRepository, passwordEncoder, userMapper);
    }
}