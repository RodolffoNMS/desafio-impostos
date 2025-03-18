package com.zup.desafio_imposto.mappers;

import com.zup.desafio_imposto.dtos.request.TaxTypeRequestDTO;
import com.zup.desafio_imposto.dtos.response.TaxTypeResponseDTO;
import com.zup.desafio_imposto.models.TaxType;
import org.springframework.stereotype.Component;

@Component
public class TaxTypeMapper {

    public TaxTypeResponseDTO toResponseDTO(TaxType taxType) {
        return new TaxTypeResponseDTO(
                taxType.getId(),
                taxType.getName(),
                taxType.getDescription(),
                taxType.getRate()
        );
    }

    public TaxType toEntity(TaxTypeRequestDTO taxRequest) {
        TaxType taxType = new TaxType();
        taxType.setName(taxRequest.name());
        taxType.setDescription(taxRequest.description());
        taxType.setRate(taxRequest.rate());

        return taxType;
    }
}