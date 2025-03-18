package com.zup.desafio_imposto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.desafio_imposto.config.security.JwtTokenProvider;
import com.zup.desafio_imposto.dtos.request.LoginRequestDTO;
import com.zup.desafio_imposto.dtos.request.UserRequestDTO;
import com.zup.desafio_imposto.dtos.response.LoginResponseDTO;
import com.zup.desafio_imposto.dtos.response.UserResponseDTO;
import com.zup.desafio_imposto.models.User;
import com.zup.desafio_imposto.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Configuration
    public class SecurityConfig {
        @Bean
        public JwtTokenProvider jwtTokenProvider() {
            return new JwtTokenProvider();
        }
    }


    @Test
    void shouldGetAllUsers() throws Exception {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("password1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setPassword("password2");

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        // Act & Assert
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    void shouldRegisterUser() throws Exception {
        // Arrange
        Set<String> roles = Set.of("ROLE_USER"); // Cria um Set<String> com o papel "ROLE_USER"
        UserRequestDTO userRequest = new UserRequestDTO("newUser", "newPassword", roles); // Passa o Set<String> para o DTO
        UserResponseDTO userResponse = new UserResponseDTO(1L, "newUser", roles); // Passa o Set<String> para o DTO

        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("newUser"))
                .andExpect(jsonPath("$.role[0]").value("ROLE_USER")); // Corrigido para "role[0]"
    }


    @Test
    void shouldLoginUser() throws Exception {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO("validUser", "validPassword");
        LoginResponseDTO loginResponse = new LoginResponseDTO("validToken");

        when(userService.loginUser(any(LoginRequestDTO.class))).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("validToken"));
    }
}
