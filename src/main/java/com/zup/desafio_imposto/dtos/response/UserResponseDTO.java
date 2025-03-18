package com.zup.desafio_imposto.dtos.response;

import java.util.Set;

public record UserResponseDTO (Long id, String username, Set<String> role)
{}
