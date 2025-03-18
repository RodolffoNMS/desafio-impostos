package com.zup.desafio_imposto.controllers;

import com.zup.desafio_imposto.dtos.request.LoginRequestDTO;
import com.zup.desafio_imposto.dtos.request.UserRequestDTO;
import com.zup.desafio_imposto.dtos.response.LoginResponseDTO;
import com.zup.desafio_imposto.dtos.response.UserResponseDTO;
import com.zup.desafio_imposto.models.User;
import com.zup.desafio_imposto.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Gerenciamento de usuários e autenticação")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados no sistema.")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @Operation(summary = "Registrar um novo usuário", description = "Cadastra um novo usuário no sistema com base nos dados fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequestDTO userRequest) {
            UserResponseDTO userResponse = userService.createUser(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);

    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar um usuário", description = "Realiza a autenticação de um usuário com base nas credenciais fornecidas.")
    //Alteração do tipo de retorno para ResponseEntity<?>: Permite retornar diferentes tipos de objetos no corpo da resposta
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO loginResponse = userService.loginUser(loginRequest);
        return ResponseEntity.ok().body(loginResponse);

    }
}