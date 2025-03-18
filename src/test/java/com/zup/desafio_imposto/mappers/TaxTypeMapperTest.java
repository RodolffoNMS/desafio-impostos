package com.zup.desafio_imposto.mappers;

import com.zup.desafio_imposto.dtos.request.TaxTypeRequestDTO;
import com.zup.desafio_imposto.dtos.response.TaxTypeResponseDTO;
import com.zup.desafio_imposto.models.TaxType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaxTypeMapperTest {

    private final TaxTypeMapper taxTypeMapper = new TaxTypeMapper(); // Instância da classe TaxTypeMapper

    @Test
    void shouldMapTaxTypeToResponseDTO() {
        // Arrange
        TaxType taxType = new TaxType();
        taxType.setId(1L);
        taxType.setName("VAT");
        taxType.setDescription("Value Added Tax");
        taxType.setRate(15.0);

        // Act
        TaxTypeResponseDTO responseDTO = taxTypeMapper.toResponseDTO(taxType);

        // Assert
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.id()).isEqualTo(1L);
        assertThat(responseDTO.name()).isEqualTo("VAT");
        assertThat(responseDTO.description()).isEqualTo("Value Added Tax");
        assertThat(responseDTO.rate()).isEqualTo(15.0);
    }

    @Test
    void shouldMapTaxTypeRequestDTOToEntity() {
        // Arrange
        TaxTypeRequestDTO requestDTO = new TaxTypeRequestDTO("Income Tax", "Tax on income", 20.0);

        // Act
        TaxType taxType = taxTypeMapper.toEntity(requestDTO);

        // Assert
        assertThat(taxType).isNotNull();
        assertThat(taxType.getName()).isEqualTo("Income Tax");
        assertThat(taxType.getDescription()).isEqualTo("Tax on income");
        assertThat(taxType.getRate()).isEqualTo(20.0);
    }
}