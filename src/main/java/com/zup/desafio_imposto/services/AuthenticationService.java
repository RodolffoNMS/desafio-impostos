package com.zup.desafio_imposto.services;

import com.zup.desafio_imposto.dtos.request.LoginRequestDTO;
import com.zup.desafio_imposto.dtos.response.LoginResponseDTO;


public interface AuthenticationService {

    LoginResponseDTO authenticate(LoginRequestDTO loginRequest);

}
