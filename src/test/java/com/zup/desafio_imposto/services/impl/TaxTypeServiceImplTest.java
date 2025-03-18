package com.zup.desafio_imposto.services.impl;

import com.zup.desafio_imposto.dtos.request.CalculateTaxTypeRequestDTO;
import com.zup.desafio_imposto.dtos.request.TaxTypeRequestDTO;
import com.zup.desafio_imposto.dtos.response.CalculateTaxTypeResponseDTO;
import com.zup.desafio_imposto.dtos.response.TaxTypeResponseDTO;
import com.zup.desafio_imposto.exceptions.TaxNotFoundException;
import com.zup.desafio_imposto.mappers.TaxTypeMapper;
import com.zup.desafio_imposto.models.TaxType;
import com.zup.desafio_imposto.repositories.TaxTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaxTypeServiceImplTest {

    @Mock
    private TaxTypeRepository taxTypeRepository;

    @Mock
    private TaxTypeMapper taxTypeMapper;

    @InjectMocks
    private TaxTypeServiceImpl taxTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllTaxTypes_WhenFindAllIsCalled() {
        // Arrange
        TaxType taxType = new TaxType(1L, "Tax1", "Description of Tax1", 10.0);
        TaxTypeResponseDTO responseDTO = new TaxTypeResponseDTO(1L, "Tax1", "Description of Tax1", 10.0);
        when(taxTypeRepository.findAll()).thenReturn(List.of(taxType));
        when(taxTypeMapper.toResponseDTO(taxType)).thenReturn(responseDTO);

        // Act
        List<TaxTypeResponseDTO> result = taxTypeService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Tax1", result.get(0).name());
        verify(taxTypeRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnTaxTypeById_WhenIdExists() {
        // Arrange
        TaxType taxType = new TaxType(1L, "Tax1", "Description of Tax1",10.0);
        TaxTypeResponseDTO responseDTO = new TaxTypeResponseDTO(1L, "Tax1", "Description of Tax1", 10.0);
        when(taxTypeRepository.findById(1L)).thenReturn(Optional.of(taxType));
        when(taxTypeMapper.toResponseDTO(taxType)).thenReturn(responseDTO);

        // Act
        TaxTypeResponseDTO result = taxTypeService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Tax1", result.name());
        verify(taxTypeRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowException_WhenTaxTypeIdDoesNotExist() {
        // Arrange
        when(taxTypeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaxNotFoundException.class, () -> taxTypeService.findById(1L));
        verify(taxTypeRepository, times(1)).findById(1L);
    }

    @Test
    void shouldAddNewTaxType_WhenValidRequestIsProvided() {
        // Arrange
        TaxTypeRequestDTO requestDTO = new TaxTypeRequestDTO("Tax1", "Description of Tax1",10.0);
        TaxType taxType = new TaxType(null, "Tax1", "Description of Tax1", 10.0);
        TaxType savedTaxType = new TaxType(1L, "Tax1", "Description of Tax1",10.0);
        TaxTypeResponseDTO responseDTO = new TaxTypeResponseDTO(1L, "Tax1", "Description of Tax1", 10.0);

        when(taxTypeMapper.toEntity(requestDTO)).thenReturn(taxType);
        when(taxTypeRepository.save(taxType)).thenReturn(savedTaxType);
        when(taxTypeMapper.toResponseDTO(savedTaxType)).thenReturn(responseDTO);

        // Act
        TaxTypeResponseDTO result = taxTypeService.addTax(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Tax1", result.name());
        verify(taxTypeRepository, times(1)).save(taxType);
    }

    @Test
    void shouldCalculateTaxCorrectly_WhenValidRequestIsProvided() {
        // Arrange
        CalculateTaxTypeRequestDTO requestDTO = new CalculateTaxTypeRequestDTO(1L, 100.0);
        TaxType taxType = new TaxType(1L, "TaxRESULT", "Description of TaxRESULT", 10.0);
        when(taxTypeRepository.findById(1L)).thenReturn(Optional.of(taxType));

        // Act
        CalculateTaxTypeResponseDTO result = taxTypeService.calculateTaxType(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("TaxRESULT", result.taxName());
        assertEquals(10.0, result.rate());
        assertEquals(10.0, result.taxCalculated());
        verify(taxTypeRepository, times(1)).findById(1L);
    }

    @Test
    void shouldDeleteTaxType_WhenIdExists() {
        // Arrange
        when(taxTypeRepository.existsById(1L)).thenReturn(true);

        // Act
        taxTypeService.deleteTaxById(1L);

        // Assert
        verify(taxTypeRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowException_WhenDeletingTaxTypeWithNonExistentId() {
        // Arrange
        when(taxTypeRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(TaxNotFoundException.class, () -> taxTypeService.deleteTaxById(1L));
        verify(taxTypeRepository, never()).deleteById(1L);
    }
}