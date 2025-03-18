package com.zup.desafio_imposto.dtos.response;

public record CalculateTaxTypeResponseDTO (
        String taxName,
        Double baseValue,
        Double rate,
        Double taxCalculated
)
{}