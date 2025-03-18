package com.zup.desafio_imposto.mappers;

import com.zup.desafio_imposto.dtos.request.UserRequestDTO;
import com.zup.desafio_imposto.models.Role;
import com.zup.desafio_imposto.models.User;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper(); // Instância da classe UserMapper

    @Test
    void shouldMapUserRequestDTOToEntity() {
        // Arrange
        Set<String> roles = Set.of("ROLE_USER", "ROLE_ADMIN");
        UserRequestDTO userRequest = new UserRequestDTO("john_doe", "raw_password", roles);
        String encodedPassword = "encoded_password";
        Set<Role> roleEntities = Set.of(new Role(1L, "ROLE_USER"), new Role(2L, "ROLE_ADMIN"));

        // Act
        User user = userMapper.toEntity(userRequest, encodedPassword, roleEntities);

        // Assert
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("john_doe");
        assertThat(user.getPassword()).isEqualTo(encodedPassword);
        assertThat(user.getRoles()).hasSize(2);
        assertThat(user.getRoles()).extracting(Role::getName).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }
}
