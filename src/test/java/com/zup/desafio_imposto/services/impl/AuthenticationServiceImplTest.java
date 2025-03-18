package com.zup.desafio_imposto.services.impl;

import com.zup.desafio_imposto.config.security.JwtTokenProvider;
import com.zup.desafio_imposto.dtos.request.LoginRequestDTO;
import com.zup.desafio_imposto.dtos.response.LoginResponseDTO;
import com.zup.desafio_imposto.models.Role;
import com.zup.desafio_imposto.models.User;
import com.zup.desafio_imposto.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {

    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        System.setProperty("JWT_SECRET_KEY", "mySecretKey");
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationServiceImpl(userRepository, passwordEncoder, jwtTokenProvider);
    }

    @Test
    void shouldMockJwtTokenProviderSuccessfully() {
        // Arrange
        String username = "validUser";
        String roles = "ROLE_USER,ROLE_ADMIN";
        String expectedToken = "generatedToken";

        when(jwtTokenProvider.generateToken(username, roles)).thenReturn(expectedToken);

        // Act
        String token = jwtTokenProvider.generateToken(username, roles);

        // Assert
        assertEquals(expectedToken, token);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        String username = "invalidUser";
        String password = "password";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        LoginRequestDTO loginRequest = new LoginRequestDTO(username, password);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.authenticate(loginRequest);
        });

        assertEquals("Usuário não encontrado!", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        // Arrange
        String username = "validUser";
        String password = "invalidPassword";
        String encodedPassword = "encodedPassword";

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        LoginRequestDTO loginRequest = new LoginRequestDTO(username, password);

        // Act & Assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authenticationService.authenticate(loginRequest);
        });

        assertEquals("Senha inválida!", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString());
    }


    @Test
    void shouldExtractRolesCorrectly() {
        // Arrange
        String username = "validUser";
        String password = "validPassword";
        String encodedPassword = "encodedPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRoles(Set.of(new Role(1L, "ROLE_USER"), new Role(2L, "ROLE_ADMIN")));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtTokenProvider.generateToken(eq(username), anyString())).thenReturn("dummyToken");

        LoginRequestDTO loginRequest = new LoginRequestDTO(username, password);

        // Act
        LoginResponseDTO response = authenticationService.authenticate(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("dummyToken", response.token());
        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

}