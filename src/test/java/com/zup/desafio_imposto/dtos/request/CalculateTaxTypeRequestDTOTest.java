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

class CalculateTaxTypeRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidation_WhenAllFieldsAreValid() {
        // Arrange
        CalculateTaxTypeRequestDTO validDTO = new CalculateTaxTypeRequestDTO(1L, 100.0);

        // Act
        Set<ConstraintViolation<CalculateTaxTypeRequestDTO>> violations = validator.validate(validDTO);

        // Assert
        assertTrue(violations.isEmpty(), "There should be no validation errors");
    }

    @Test
    void shouldFailValidation_WhenTaxIdIsNull() {
        // Arrange
        CalculateTaxTypeRequestDTO invalidDTO = new CalculateTaxTypeRequestDTO(null, 100.0);

        // Act
        Set<ConstraintViolation<CalculateTaxTypeRequestDTO>> violations = validator.validate(invalidDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Por favor, insira o ID do imposto.", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidation_WhenTaxIdIsNegative() {
        // Arrange
        CalculateTaxTypeRequestDTO invalidDTO = new CalculateTaxTypeRequestDTO(-1L, 100.0);

        // Act
        Set<ConstraintViolation<CalculateTaxTypeRequestDTO>> violations = validator.validate(invalidDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("O id do imposto deve ser maior que zero", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidation_WhenTaxIdIsZero() {
        // Arrange
        CalculateTaxTypeRequestDTO invalidDTO = new CalculateTaxTypeRequestDTO(0L, 100.0);

        // Act
        Set<ConstraintViolation<CalculateTaxTypeRequestDTO>> violations = validator.validate(invalidDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("O id do imposto deve ser maior que zero", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidation_WhenBaseValueIsNull() {
        // Arrange
        CalculateTaxTypeRequestDTO invalidDTO = new CalculateTaxTypeRequestDTO(1L, null);

        // Act
        Set<ConstraintViolation<CalculateTaxTypeRequestDTO>> violations = validator.validate(invalidDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Por favor, insira o valor base", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidation_WhenBaseValueIsNegative() {
        // Arrange
        CalculateTaxTypeRequestDTO invalidDTO = new CalculateTaxTypeRequestDTO(1L, -100.0);

        // Act
        Set<ConstraintViolation<CalculateTaxTypeRequestDTO>> violations = validator.validate(invalidDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("O valor base deve ser maior que zero.", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidation_WhenBaseValueIsZero() {
        // Arrange
        CalculateTaxTypeRequestDTO invalidDTO = new CalculateTaxTypeRequestDTO(1L, 0.0);

        // Act
        Set<ConstraintViolation<CalculateTaxTypeRequestDTO>> violations = validator.validate(invalidDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("O valor base deve ser maior que zero.", violations.iterator().next().getMessage());
    }
}