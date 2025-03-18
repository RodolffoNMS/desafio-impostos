package com.zup.desafio_imposto.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TaxTypeRequestDTO (
        @NotBlank(message = "Por favor, insira o nome do Imposto.")
        @JsonProperty("nome") // "nome" -> "name"
        String name,

        @NotBlank(message = "Por favor, insira a descrição do Imposto.")
        @JsonProperty("descricao")
        String description,

        @NotNull(message = "Por favor, insira o valor da alíquota.")
        @Positive(message = "A alíquota deve ser maior que zero")
        @JsonProperty("aliquota")
        Double rate
) {
}