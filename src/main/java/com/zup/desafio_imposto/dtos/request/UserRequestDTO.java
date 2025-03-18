package com.zup.desafio_imposto.dtos.request;

import com.zup.desafio_imposto.dtos.Roles;
import com.zup.desafio_imposto.validation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UserRequestDTO (
        @NotBlank(message = "Insira o nome de usuário.")
        String username,

        @NotBlank(message = "Insira uma senha.")
        String password,

        @NotEmpty(message = "Insira a role do usuário.")
        Set<@ValidEnum(enumClass = Roles.class, message = "Valor inválido para o campo role.\nValores disponíveis:\nROLE_ADMIN\nROLE_USER")
        String> role //ADMIN ou USER
)
{}