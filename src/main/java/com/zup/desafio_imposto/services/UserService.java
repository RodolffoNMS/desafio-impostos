package com.zup.desafio_imposto.services;

import com.zup.desafio_imposto.dtos.request.LoginRequestDTO;
import com.zup.desafio_imposto.dtos.request.UserRequestDTO;
import com.zup.desafio_imposto.dtos.response.LoginResponseDTO;
import com.zup.desafio_imposto.dtos.response.UserResponseDTO;
import com.zup.desafio_imposto.models.User;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO userRequest);

    LoginResponseDTO loginUser (LoginRequestDTO login);

    List<User> getAllUsers();
}