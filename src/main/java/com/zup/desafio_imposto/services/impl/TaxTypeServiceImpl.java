package com.zup.desafio_imposto.services.impl;

import com.zup.desafio_imposto.dtos.request.CalculateTaxTypeRequestDTO;
import com.zup.desafio_imposto.dtos.request.TaxTypeRequestDTO;
import com.zup.desafio_imposto.dtos.response.CalculateTaxTypeResponseDTO;
import com.zup.desafio_imposto.dtos.response.TaxTypeResponseDTO;
import com.zup.desafio_imposto.exceptions.DuplicateTaxNameException;
import com.zup.desafio_imposto.exceptions.TaxNotFoundException;
import com.zup.desafio_imposto.mappers.TaxTypeMapper;
import com.zup.desafio_imposto.models.TaxType;
import com.zup.desafio_imposto.repositories.TaxTypeRepository;
import com.zup.desafio_imposto.services.TaxTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxTypeServiceImpl implements TaxTypeService {

    private final TaxTypeRepository taxTypeRepository;
    private final TaxTypeMapper taxTypeMapper;

    public TaxTypeServiceImpl(TaxTypeRepository taxTypeRepository, TaxTypeMapper taxTypeMapper) {
        this.taxTypeRepository = taxTypeRepository;
        this.taxTypeMapper = taxTypeMapper;
    }

    @Override
    public List<TaxTypeResponseDTO> findAll() {
        return taxTypeRepository.findAll()
                .stream()
                .map(taxTypeMapper::toResponseDTO)
                .toList();
    }

    @Override
    public TaxTypeResponseDTO findById(Long id) {
        TaxType taxType = findTaxByIdOrThrow(id);
        return taxTypeMapper.toResponseDTO(taxType);
    }

    @Override
    public TaxTypeResponseDTO addTax(TaxTypeRequestDTO taxRequest) {
        TaxType taxType = taxTypeMapper.toEntity(taxRequest);
        TaxType savedTaxType = taxTypeRepository.save(taxType);
        return taxTypeMapper.toResponseDTO(savedTaxType);
    }

    @Override
    public CalculateTaxTypeResponseDTO calculateTaxType(CalculateTaxTypeRequestDTO calculateTaxRequest) {
        TaxType taxType = findTaxByIdOrThrow(calculateTaxRequest.taxId()); // Obtém o imposto pelo ID
        double taxValue = calculateTaxValue(taxType.getRate(), calculateTaxRequest.baseValue()); // Calcula o valor do imposto
        return new CalculateTaxTypeResponseDTO(
                taxType.getName(), // Nome do imposto
                calculateTaxRequest.baseValue(), // Valor base
                taxType.getRate(), // Taxa do imposto
                taxValue // Valor calculado do imposto
        );
    }

    @Override
    public void deleteTaxById(Long id) {
        if (!taxTypeRepository.existsById(id)) {
            throw new TaxNotFoundException("Imposto não encontrado");
        }
        taxTypeRepository.deleteById(id);
    }

    private TaxType findTaxByIdOrThrow(Long id) {
        return taxTypeRepository.findById(id)
                .orElseThrow(() -> new TaxNotFoundException("Imposto não encontrado"));
    }

    private double calculateTaxValue(double rate, double baseValue) {
        return baseValue * rate / 100.0;
    }

    private void validateDuplicateTaxName(String name){
        if (taxTypeRepository.existsByName(name)) {
            throw new DuplicateTaxNameException("Imposto já cadastrado no sistema");
        }
    }
}