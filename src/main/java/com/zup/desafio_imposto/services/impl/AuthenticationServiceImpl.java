package com.zup.desafio_imposto.services.impl;

import com.zup.desafio_imposto.config.security.JwtTokenProvider;
import com.zup.desafio_imposto.dtos.request.LoginRequestDTO;
import com.zup.desafio_imposto.dtos.response.LoginResponseDTO;
import com.zup.desafio_imposto.models.Role;
import com.zup.desafio_imposto.models.User;
import com.zup.desafio_imposto.repositories.UserRepository;
import com.zup.desafio_imposto.services.AuthenticationService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO loginRequest) {
        // Valida se o loginRequest não é nulo
        if (loginRequest == null) {
            throw new IllegalArgumentException("Requisição de login não pode ser nula!");
        }

        // Busca o usuário pelo username
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));

        // Valida a senha
        validatePassword(loginRequest.password(), user.getPassword());

        // Extrai os papéis do usuário
        String roles = extractRoles(user);

        // Gera o token JWT
        String token = generateToken(user.getUsername(), roles);

        // Retorna o token no DTO de resposta
        return new LoginResponseDTO(token);
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        // Verifica se a senha fornecida corresponde à senha armazenada
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BadCredentialsException("Senha inválida!");
        }
    }

    private String extractRoles(User user) {
        // Concatena os nomes dos papéis em uma string separada por vírgulas
        return user.getRoles().stream()
                .map(Role::getName) // Supondo que Role tenha o método getName()
                .collect(Collectors.joining(","));
    }

    private String generateToken(String username, String roles) {
        // Gera o token JWT utilizando o JwtTokenProvider
        return jwtTokenProvider.generateToken(username, roles);
    }
}
