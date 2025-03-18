package com.zup.desafio_imposto.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CalculateTaxTypeRequestDTO (
        @NotNull(message = "Por favor, insira o ID do imposto.")
        @Positive(message = "O id do imposto deve ser maior que zero")
        Long taxId,

        @NotNull(message = "Por favor, insira o valor base")
        @Positive(message = "O valor base deve ser maior que zero.")
        Double baseValue
) {}