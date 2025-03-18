package com.zup.desafio_imposto.services;

import com.zup.desafio_imposto.dtos.request.CalculateTaxTypeRequestDTO;
import com.zup.desafio_imposto.dtos.request.TaxTypeRequestDTO;
import com.zup.desafio_imposto.dtos.response.CalculateTaxTypeResponseDTO;
import com.zup.desafio_imposto.dtos.response.TaxTypeResponseDTO;

import java.util.List;
public interface TaxTypeService {

    public List<TaxTypeResponseDTO> findAll();

    TaxTypeResponseDTO findById(Long id);

    TaxTypeResponseDTO addTax(TaxTypeRequestDTO taxRequest);

    CalculateTaxTypeResponseDTO calculateTaxType(CalculateTaxTypeRequestDTO calculateTaxRequest);

    void deleteTaxById(Long id);
}
