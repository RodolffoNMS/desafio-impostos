package com.zup.desafio_imposto.controllers;

import com.zup.desafio_imposto.dtos.request.CalculateTaxTypeRequestDTO;
import com.zup.desafio_imposto.dtos.request.TaxTypeRequestDTO;
import com.zup.desafio_imposto.dtos.response.CalculateTaxTypeResponseDTO;
import com.zup.desafio_imposto.dtos.response.TaxTypeResponseDTO;
import com.zup.desafio_imposto.exceptions.GlobalExceptionHandler;
import com.zup.desafio_imposto.services.TaxTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TaxTypeControllerTest {

    @Mock
    private TaxTypeService taxTypeService;

    @InjectMocks
    private TaxTypeController taxTypeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllTaxTypes_WhenTaxTypesExist() {
        // Arrange
        TaxTypeResponseDTO tax1 = new TaxTypeResponseDTO(1L, "Imposto A", "Descrição A", 10.0);
        TaxTypeResponseDTO tax2 = new TaxTypeResponseDTO(2L, "Imposto B", "Descrição B", 15.0);
        List<TaxTypeResponseDTO> mockTaxTypes = Arrays.asList(tax1, tax2);

        when(taxTypeService.findAll()).thenReturn(mockTaxTypes);

        // Act
        ResponseEntity<List<TaxTypeResponseDTO>> response = taxTypeController.getAllTaxTypes();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(taxTypeService, times(1)).findAll();
    }

    @Test
    void shouldReturnTaxTypeById_WhenTaxTypeExists() {
        // Arrange
        TaxTypeResponseDTO taxTypeResponse = new TaxTypeResponseDTO(1L, "Imposto A", "Descrição A", 10.0);
        when(taxTypeService.findById(1L)).thenReturn(taxTypeResponse);

        // Act
        ResponseEntity<TaxTypeResponseDTO> response = taxTypeController.getTaxTypeById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taxTypeResponse, response.getBody());
        verify(taxTypeService, times(1)).findById(1L);
    }

    @Test
    void shouldAddTaxTypeSuccessfully_WhenRequestIsValid() {
        // Arrange
        TaxTypeRequestDTO taxTypeRequest = new TaxTypeRequestDTO("Imposto A", "Descrição A", 10.0);
        TaxTypeResponseDTO taxTypeResponse = new TaxTypeResponseDTO(1L, "Imposto A", "Descrição A", 60.0);

        when(taxTypeService.addTax(taxTypeRequest)).thenReturn(taxTypeResponse);

        // Act
        ResponseEntity<TaxTypeResponseDTO> response = taxTypeController.addTaxType(taxTypeRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(taxTypeResponse, response.getBody());
        verify(taxTypeService, times(1)).addTax(taxTypeRequest);
    }

    @Test
    void shouldCalculateTaxTypeSuccessfully_WhenRequestIsValid() {
        // Arrange
        CalculateTaxTypeRequestDTO calculateRequest = new CalculateTaxTypeRequestDTO(1L, 100.0);
        CalculateTaxTypeResponseDTO calculateResponse = new CalculateTaxTypeResponseDTO("Imposto A", 100.0, 10.0, 10.0);

        when(taxTypeService.calculateTaxType(calculateRequest)).thenReturn(calculateResponse);

        // Act
        ResponseEntity<CalculateTaxTypeResponseDTO> response = taxTypeController.calculateTaxType(calculateRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(calculateResponse, response.getBody());
        verify(taxTypeService, times(1)).calculateTaxType(calculateRequest);
    }

    @Test
    void shouldDeleteTaxTypeSuccessfully_WhenTaxTypeExists() {
        // Arrange
        Long taxTypeId = 1L;

        // Act
        ResponseEntity<Void> response = taxTypeController.deleteTaxType(taxTypeId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taxTypeService, times(1)).deleteTaxById(taxTypeId);
    }

    @Test
    void shouldHandleTaxTypeNotFound_WhenTaxTypeDoesNotExist() {
        // Arrange
        Long taxTypeId = 1L;
        when(taxTypeService.findById(taxTypeId)).thenThrow(new RuntimeException("Tipo de imposto não encontrado"));

        // Act
        ResponseEntity<?> response = null;
        try {
            taxTypeController.getTaxTypeById(taxTypeId);
        } catch (RuntimeException ex) {
            response = new GlobalExceptionHandler().handleGenericException(ex);
        }

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof GlobalExceptionHandler.ErrorResponse); // Verifica se o corpo é do tipo ErrorResponse
        GlobalExceptionHandler.ErrorResponse responseBody = (GlobalExceptionHandler.ErrorResponse) response.getBody();
        assertEquals("Erro Interno do Servidor", responseBody.error());
        assertEquals("Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.", responseBody.message());
        verify(taxTypeService, times(1)).findById(taxTypeId);
    }
}
