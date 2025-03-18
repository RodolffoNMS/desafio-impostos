package com.zup.desafio_imposto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.desafio_imposto.dtos.request.UserRequestDTO;
import com.zup.desafio_imposto.dtos.response.UserResponseDTO;
import com.zup.desafio_imposto.exceptions.DuplicateUsernameException;
import com.zup.desafio_imposto.services.UserService;
import com.zup.desafio_imposto.config.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }

        @Bean
        public JwtTokenProvider jwtTokenProvider() {
            return Mockito.mock(JwtTokenProvider.class);
        }
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO("newUser", "password123", Set.of("ROLE_USER"));
        UserResponseDTO userResponseDTO = new UserResponseDTO(1L, "newUser", Set.of("ROLE_USER"));

        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("newUser"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    void shouldNotRegisterUser_WhenUsernameAlreadyExists() throws Exception {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO("existingUser", "password123", Set.of("ROLE_USER"));
        when(userService.createUser(any(UserRequestDTO.class)))
                .thenThrow(new DuplicateUsernameException("Usuário já cadastrado no sistema"));

        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isConflict()) // Alterado para 409
                .andExpect(jsonPath("$.error").value("Conflito"))
                .andExpect(jsonPath("$.message").value("Usuário já cadastrado no sistema"));
    }

}