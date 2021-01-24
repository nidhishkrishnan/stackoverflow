package com.employee.controller;

import com.employee.config.AbstractIT;
import com.employee.domain.UserDTO;
import com.employee.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

class UserControllerIT extends AbstractIT {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    @DisplayName("Testing to get all the user details")
    void getAllUsersTest() {
        ResponseEntity<List<UserDTO>> response = this.template.exchange("/v1/users", GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<UserDTO>>() {
        });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotEmpty()
                .hasSize(3)
                .extracting(UserDTO::getEmail).containsAnyOf("user1@some.com");
    }

    @Test
    @Order(2)
    @DisplayName("Testing for saving user details")
    void saveUsersTest() {
        ResponseEntity<Void> response = this.template.postForEntity("/v1/users", new HttpEntity<>(buildUserRequest()), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        long usersCount = userRepository.count();
        assertThat(usersCount).isEqualTo(4);
    }

    private UserDTO buildUserRequest() {
        return UserDTO.builder()
                .email("user4@some.com")
                .lastName("Maria")
                .firstName("Fernandes")
                .build();
    }

    @Test
    @Order(3)
    @DisplayName("Testing to deleting user details by id")
    void deleteUsersTest() {

        this.template.delete("/v1/users/{id}", singleParam("id", "21"));

        boolean userExists = userRepository.existsById(21L);
        assertThat(userExists).isFalse();
    }

    @Test
    @Order(4)
    @DisplayName("Testing to finding user details by id")
    void findUserByIdTest() {
        ResponseEntity<UserDTO> response = this.template.getForEntity("/v1/users/{id}", UserDTO.class, singleParam("id", "22"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody())
                .extracting(UserDTO::getEmail, UserDTO::getFirstName, UserDTO::getLastName)
                .containsExactly("user2@some.com", "John", "Duke");
    }

    private Map<String, String> singleParam(String key, String value) {
        Map<String, String> params = new HashMap<>();
        params.put(key, value);
        return params;
    }
}

