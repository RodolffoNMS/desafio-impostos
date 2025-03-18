package com.zup.desafio_imposto.controllers;

import com.zup.desafio_imposto.dtos.request.UserRequestDTO;
import com.zup.desafio_imposto.dtos.response.UserResponseDTO;
import com.zup.desafio_imposto.exceptions.DuplicateTaxNameException;
import com.zup.desafio_imposto.exceptions.DuplicateUsernameException;
import com.zup.desafio_imposto.exceptions.GlobalExceptionHandler;
import com.zup.desafio_imposto.models.User;
import com.zup.desafio_imposto.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllUsers() {
        // Arrange: Configurar o comportamento do mock
        User user1 = new User(1L, "usuario1", "senha1", null);
        User user2 = new User(2L, "usuario2", "senha2", null);
        List<User> mockUsers = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(mockUsers);

        // Act: Chamar o método que queremos testar
        List<User> result = userController.getAllUsers();

        // Assert: Verificar se o resultado está correto
        assertEquals(2, result.size());
        assertEquals("usuario1", result.get(0).getUsername());
        assertEquals("usuario2", result.get(1).getUsername());

        // Verificar se o método do mock foi chamado
        verify(userService, times(1)).getAllUsers();
    }
    @Test
    void shouldRegisterUserSuccessfully() {
        // Arrange: Configurar o comportamento do mock
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        UserRequestDTO userRequestDTO = new UserRequestDTO("usuario1", "senha1", roles);
        UserResponseDTO userResponseDTO = new UserResponseDTO(1L, "usuario1", roles);

        when(userService.createUser(userRequestDTO)).thenReturn(userResponseDTO);

        // Act: Chamar o método que queremos testar
        ResponseEntity<?> response = userController.registerUser(userRequestDTO);

        // Assert: Verificar se o resultado está correto
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponseDTO, response.getBody());

        // Verificar se o método do mock foi chamado
        verify(userService, times(1)).createUser(userRequestDTO);
    }

    @Test
    void shouldReturnConflictWhenUsernameIsDuplicate() {
        // Arrange: Configurar o comportamento do mock para lançar uma exceção
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        UserRequestDTO userRequestDTO = new UserRequestDTO("usuario1", "senha1", roles);

        when(userService.createUser(userRequestDTO)).thenThrow(new DuplicateUsernameException("Usuário já existe"));

        // Act: Chamar o método que queremos testar
        ResponseEntity<?> response = null;
        try {
            userController.registerUser(userRequestDTO);
        } catch (DuplicateUsernameException ex) {
            // Simular o comportamento do GlobalExceptionHandler
            response = new GlobalExceptionHandler().handleDuplicateUsernameException(ex);
        }

        // Assert: Verificar se a resposta está correta
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody() instanceof GlobalExceptionHandler.ErrorResponse); // Verifica se o corpo é do tipo ErrorResponse

        GlobalExceptionHandler.ErrorResponse responseBody = (GlobalExceptionHandler.ErrorResponse) response.getBody();
        assertEquals("Conflito", responseBody.error());
        assertEquals("Usuário já existe", responseBody.message());

        // Verificar se o método do mock foi chamado
        verify(userService, times(1)).createUser(userRequestDTO);
    }

    @Test
    void shouldReturnInternalServerErrorForUnexpectedError() {
        // Arrange: Configurar o comportamento do mock para lançar uma exceção genérica
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        UserRequestDTO userRequestDTO = new UserRequestDTO("usuario1", "senha1", roles);

        when(userService.createUser(userRequestDTO)).thenThrow(new RuntimeException("Erro inesperado"));

        // Act: Chamar o método que queremos testar
        ResponseEntity<?> response = null;
        try {
            userController.registerUser(userRequestDTO);
        } catch (RuntimeException ex) {
            // Simular o comportamento do GlobalExceptionHandler
            response = new GlobalExceptionHandler().handleGenericException(ex);
        }

        // Assert: Verificar se a resposta está correta
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof GlobalExceptionHandler.ErrorResponse); // Verifica se o corpo é do tipo ErrorResponse

        GlobalExceptionHandler.ErrorResponse responseBody = (GlobalExceptionHandler.ErrorResponse) response.getBody();
        assertEquals("Erro Interno do Servidor", responseBody.error());
        assertEquals("Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.", responseBody.message());

        // Verificar se o método do mock foi chamado
        verify(userService, times(1)).createUser(userRequestDTO);
    }
    @Test
    void shouldReturnConflictWhenTaxNameIsDuplicate() {
        // Arrange: Configurar o comportamento do mock para lançar uma exceção
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        UserRequestDTO userRequestDTO = new UserRequestDTO("usuario1", "senha1", roles);

        when(userService.createUser(userRequestDTO)).thenThrow(new DuplicateTaxNameException("Nome de imposto duplicado"));

        // Act: Chamar o método que queremos testar
        ResponseEntity<?> response = null;
        try {
            userController.registerUser(userRequestDTO);
        } catch (DuplicateTaxNameException ex) {
            // Simular o comportamento do GlobalExceptionHandler
            response = new GlobalExceptionHandler().handleDuplicateTaxNameException(ex);
        }

        // Assert: Verificar se a resposta está correta
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody() instanceof GlobalExceptionHandler.ErrorResponse); // Verifica se o corpo é do tipo ErrorResponse

        GlobalExceptionHandler.ErrorResponse responseBody = (GlobalExceptionHandler.ErrorResponse) response.getBody();
        assertEquals("Conflito", responseBody.error());
        assertEquals("Nome de imposto duplicado", responseBody.message());

        // Verificar se o método do mock foi chamado
        verify(userService, times(1)).createUser(userRequestDTO);
    }
}