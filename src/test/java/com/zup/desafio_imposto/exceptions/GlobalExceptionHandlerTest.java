package com.zup.desafio_imposto.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Mock
    private HttpServletRequest mockRequest;

    public GlobalExceptionHandlerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldHandleDuplicateUsernameException() {
        // Arrange
        String errorMessage = "Username already exists";
        DuplicateUsernameException exception = new DuplicateUsernameException(errorMessage);

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = globalExceptionHandler.handleDuplicateUsernameException(exception);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Conflito", response.getBody().error());
        assertEquals(errorMessage, response.getBody().message());
    }

    @Test
    void shouldHandleTaxNotFoundException() {
        // Arrange
        String errorMessage = "Tax not found";
        TaxNotFoundException exception = new TaxNotFoundException(errorMessage);

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = globalExceptionHandler.handleTaxNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Não Encontrado", response.getBody().error());
        assertEquals(errorMessage, response.getBody().message());
    }

    @Test
    void shouldHandleUsernameNotFoundException() {
        // Arrange
        String errorMessage = "Username not found";
        UsernameNotFoundException exception = new UsernameNotFoundException(errorMessage);

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = globalExceptionHandler.handleUsernameNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Não Encontrado", response.getBody().error());
        assertEquals(errorMessage, response.getBody().message());
    }

    @Test
    void shouldHandleValidationExceptions() throws NoSuchMethodException {
        // Arrange
        FieldError fieldError = new FieldError("objectName", "fieldName", "Field is invalid");
        BindException bindException = new BindException(new Object(), "objectName");
        bindException.addError(fieldError);

        // Criando um MethodParameter válido com índice -1
        MethodParameter methodParameter = new MethodParameter(
                this.getClass().getDeclaredMethod("shouldHandleValidationExceptions"), -1
        );
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindException);

        // Act
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleValidationExceptions(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro de Validação", response.getBody().get("erro"));
        assertEquals("Field is invalid", ((Map<?, ?>) response.getBody().get("detalhes")).get("fieldName"));
    }

    @Test
    void shouldHandleConstraintViolationException() {
        // Arrange
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);

        // Mock para o Path
        jakarta.validation.Path mockPath = mock(jakarta.validation.Path.class);
        when(mockPath.toString()).thenReturn("fieldName");
        when(violation.getPropertyPath()).thenReturn(mockPath);
        when(violation.getMessage()).thenReturn("Field is invalid");

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);

        ConstraintViolationException exception = new ConstraintViolationException(violations);

        // Act
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleConstraintViolationException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro de Validação", response.getBody().get("erro"));
        assertEquals("Field is invalid", ((Map<?, ?>) response.getBody().get("detalhes")).get("fieldName"));
    }

    @Test
    void shouldHandleUnauthorizedAccess() {
        // Arrange
        when(mockRequest.getRequestURI()).thenReturn("/test-endpoint");

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = globalExceptionHandler.handleUnauthorized(mockRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Não Autorizado", response.getBody().error());
        assertEquals("Usuário não autorizado", response.getBody().message());
        verify(mockRequest, times(1)).getRequestURI();
    }

    @Test
    void shouldHandleDuplicateTaxNameException() {
        // Arrange
        String errorMessage = "Tax name already exists";
        DuplicateTaxNameException exception = new DuplicateTaxNameException(errorMessage);

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = globalExceptionHandler.handleDuplicateTaxNameException(exception);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Conflito", response.getBody().error());
        assertEquals(errorMessage, response.getBody().message());
    }

    @Test
    void shouldHandleGenericException() {
        // Arrange
        String errorMessage = "Unexpected error";
        Exception exception = new Exception(errorMessage);

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = globalExceptionHandler.handleGenericException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Erro Interno do Servidor", response.getBody().error());
        assertEquals("Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.", response.getBody().message());
    }
}