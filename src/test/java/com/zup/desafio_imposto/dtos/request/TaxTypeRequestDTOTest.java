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

class TaxTypeRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidation_WhenAllFieldsAreValid() {
        // Arrange
        TaxTypeRequestDTO validDTO = new TaxTypeRequestDTO("Imposto A", "Descrição do Imposto A", 10.0);

        // Act
        Set<ConstraintViolation<TaxTypeRequestDTO>> violations = validator.validate(validDTO);

        // Assert
        assertTrue(violations.isEmpty(), "There should be no validation errors");
    }

    @Test
    void shouldFailValidation_WhenNameIsBlank() {
        // Arrange
        TaxTypeRequestDTO invalidDTO = new TaxTypeRequestDTO("", "Descrição do Imposto A", 10.0);

        // Act
        Set<ConstraintViolation<TaxTypeRequestDTO>> violations = validator.validate(invalidDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Por favor, insira o nome do Imposto.", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidation_WhenDescriptionIsBlank() {
        // Arrange
        TaxTypeRequestDTO invalidDTO = new TaxTypeRequestDTO("Imposto A", "", 10.0);

        // Act
        Set<ConstraintViolation<TaxTypeRequestDTO>> violations = validator.validate(invalidDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Por favor, insira a descrição do Imposto.", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidation_WhenRateIsNull() {
        // Arrange
        TaxTypeRequestDTO invalidDTO = new TaxTypeRequestDTO("Imposto A", "Descrição do Imposto A", null);

        // Act
        Set<ConstraintViolation<TaxTypeRequestDTO>> violations = validator.validate(invalidDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("Por favor, insira o valor da alíquota.", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidation_WhenRateIsNegative() {
        // Arrange
        TaxTypeRequestDTO invalidDTO = new TaxTypeRequestDTO("Imposto A", "Descrição do Imposto A", -5.0);

        // Act
        Set<ConstraintViolation<TaxTypeRequestDTO>> violations = validator.validate(invalidDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("A alíquota deve ser maior que zero", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidation_WhenRateIsZero() {
        // Arrange
        TaxTypeRequestDTO invalidDTO = new TaxTypeRequestDTO("Imposto A", "Descrição do Imposto A", 0.0);

        // Act
        Set<ConstraintViolation<TaxTypeRequestDTO>> violations = validator.validate(invalidDTO);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("A alíquota deve ser maior que zero", violations.iterator().next().getMessage());
    }
}