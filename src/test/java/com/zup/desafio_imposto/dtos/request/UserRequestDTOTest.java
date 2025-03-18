package com.zup.desafio_imposto.dtos.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWhenAllFieldsAreValid() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                "validUsername",
                "validPassword",
                Set.of("ROLE_ADMIN", "ROLE_USER")
        );

        // Act
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(userRequestDTO);

        // Assert
        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    void shouldFailValidationWhenUsernameIsNull() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                null,
                "validPassword",
                Set.of("ROLE_ADMIN")
        );

        // Act
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(userRequestDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Insira o nome de usuário.", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenPasswordIsNull() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                "validUsername",
                null,
                Set.of("ROLE_ADMIN")
        );

        // Act
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(userRequestDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Insira uma senha.", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenAllFieldsAreInvalid() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                null,
                null,
                Set.of("INVALID_ROLE")
        );

        // Act
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(userRequestDTO);

        // Debug: Imprima as mensagens de erro
        violations.forEach(violation -> System.out.println("Mensagem de erro: " + violation.getMessage()));

        // Assert
        assertEquals(3, violations.size());
    }

    @Test
    void shouldFailValidationWhenUsernameIsBlank() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                "",
                "validPassword",
                Set.of("ROLE_ADMIN")
        );

        // Act
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(userRequestDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Insira o nome de usuário.", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenPasswordIsBlank() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                "validUsername",
                "",
                Set.of("ROLE_ADMIN")
        );

        // Act
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(userRequestDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Insira uma senha.", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenRoleIsEmpty() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                "validUsername",
                "validPassword",
                Set.of()
        );

        // Act
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(userRequestDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Insira a role do usuário.", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenRoleIsInvalid() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO(
                "validUsername",
                "validPassword",
                Set.of("INVALID_ROLE")
        );

        // Act
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(userRequestDTO);

        // Debug: Imprima as mensagens de erro
        violations.forEach(violation -> System.out.println("Mensagem de erro: " + violation.getMessage()));

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Ops! Esse valor não é válido. Tente um dos seguintes: ROLE_USER, ROLE_ADMIN",
                violations.iterator().next().getMessage());
    }

}