package com.zup.desafio_imposto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.desafio_imposto.config.security.JwtTokenProvider;
import com.zup.desafio_imposto.dtos.request.CalculateTaxTypeRequestDTO;
import com.zup.desafio_imposto.dtos.request.TaxTypeRequestDTO;
import com.zup.desafio_imposto.dtos.response.CalculateTaxTypeResponseDTO;
import com.zup.desafio_imposto.dtos.response.TaxTypeResponseDTO;
import com.zup.desafio_imposto.exceptions.TaxNotFoundException;
import com.zup.desafio_imposto.services.TaxTypeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TaxTypeController.class)
@AutoConfigureMockMvc(addFilters = false) // Desabilita os filtros de segurança
class TaxTypeControllerIntegrationTest {

    private static final String BASE_URL = "/tax/tipos";
    private static final String CALCULATE_URL = "/tax/calculo";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaxTypeService taxTypeService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TaxTypeService taxTypeService() {
            return Mockito.mock(TaxTypeService.class); // Mock do serviço
        }
        @Bean
        public JwtTokenProvider jwtTokenProvider() {
            return Mockito.mock(JwtTokenProvider.class);
        }
    }

    @Test
    void shouldReturnAllTaxTypes_WhenTaxTypesExist() throws Exception {
        List<TaxTypeResponseDTO> taxTypes = Arrays.asList(
                new TaxTypeResponseDTO(1L, "Imposto A", "Descrição A", 10.0),
                new TaxTypeResponseDTO(2L, "Imposto B", "Descrição B", 15.0)
        );

        when(taxTypeService.findAll()).thenReturn(taxTypes);

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Imposto A"))
                .andExpect(jsonPath("$[1].rate").value(15.0));
    }




    @Test
    void shouldReturnEmptyList_WhenNoTaxTypesExist() throws Exception {
        Mockito.when(taxTypeService.findAll()).thenReturn(List.of());

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldReturnNotFound_WhenTaxTypeIdDoesNotExist() throws Exception {
        Long id = 99L;

        Mockito.when(taxTypeService.findById(id)).thenThrow(new TaxNotFoundException("Tipo de imposto não encontrado"));

        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Tipo de imposto não encontrado"));
    }

    @Test
    void shouldReturnTaxType_WhenTaxTypeIdExists() throws Exception {
        Long id = 1L;
        TaxTypeResponseDTO taxTypeResponse = new TaxTypeResponseDTO(id, "Imposto A", "Descrição A", 10.0);

        Mockito.when(taxTypeService.findById(id)).thenReturn(taxTypeResponse);

        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Imposto A"))
                .andExpect(jsonPath("$.description").value("Descrição A"))
                .andExpect(jsonPath("$.rate").value(10.0));
    }

    @Test
    void shouldAddNewTaxType_WhenRequestIsValid() throws Exception {
        TaxTypeRequestDTO taxTypeRequest = new TaxTypeRequestDTO("Imposto A", "Descrição A", 10.0);
        TaxTypeResponseDTO taxTypeResponse = new TaxTypeResponseDTO(1L, "Imposto A", "Descrição A", 10.0);

        Mockito.when(taxTypeService.addTax(any(TaxTypeRequestDTO.class))).thenReturn(taxTypeResponse);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taxTypeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Imposto A"))
                .andExpect(jsonPath("$.rate").value(10.0));
    }

    @Test
    void shouldReturnBadRequest_WhenAddingTaxTypeWithInvalidData() throws Exception {
        TaxTypeRequestDTO taxTypeRequest = new TaxTypeRequestDTO("", "", null);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taxTypeRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes.name").value("Por favor, insira o nome do Imposto."))
                .andExpect(jsonPath("$.detalhes.description").value("Por favor, insira a descrição do Imposto."))
                .andExpect(jsonPath("$.detalhes.rate").value("Por favor, insira o valor da alíquota."));
    }

    @Test
    void shouldCalculateTaxSuccessfully_WhenRequestIsValid() throws Exception {
        CalculateTaxTypeRequestDTO calculateRequest = new CalculateTaxTypeRequestDTO(1L, 100.0);
        CalculateTaxTypeResponseDTO calculateResponse = new CalculateTaxTypeResponseDTO("Imposto A", 100.0, 10.0, 10.0);

        Mockito.when(taxTypeService.calculateTaxType(any(CalculateTaxTypeRequestDTO.class))).thenReturn(calculateResponse);

        mockMvc.perform(post(CALCULATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(calculateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseValue").value(100.0))
                .andExpect(jsonPath("$.taxCalculated").value(10.0));
    }

    @Test
    void shouldReturnBadRequest_WhenCalculatingTaxWithInvalidData() throws Exception {
        CalculateTaxTypeRequestDTO calculateRequest = new CalculateTaxTypeRequestDTO(null, -1.0);

        mockMvc.perform(post(CALCULATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(calculateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes.taxId").value("Por favor, insira o ID do imposto."))
                .andExpect(jsonPath("$.detalhes.baseValue").value("O valor base deve ser maior que zero."));
    }

    @Test
    void shouldDeleteTaxTypeSuccessfully_WhenTaxTypeIdExists() throws Exception {
        Long id = 1L;

        doNothing().when(taxTypeService).deleteTaxById(id);

        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFound_WhenDeletingTaxTypeWithNonExistentId() throws Exception {
        Long id = 1L;

        doThrow(new TaxNotFoundException("Tipo de imposto não encontrado")).when(taxTypeService).deleteTaxById(id);

        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Tipo de imposto não encontrado"));
    }
}