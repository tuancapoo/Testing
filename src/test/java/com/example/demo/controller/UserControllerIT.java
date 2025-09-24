package com.example.demo.controller;

import java.util.List;
import java.awt.PageAttributes.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.Entities.User;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
//import List

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void createUser_ShouldReturnUser_WhenEmailIsValid() throws Exception {
        // arrange
        User inputUser = new User(null, "test@example.com", "password");
        // action
        String resultString = mockMvc.perform(
                post("/users")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(inputUser)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // assert
        System.out.println("Result: " + resultString);
        User resultUser = objectMapper.readValue(resultString, User.class);
        assert resultUser.getEmail().equals(inputUser.getEmail());

    }

    @Test
    public void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        // arrange
        User user1 = new User(null, "tuan", "tuan@example.com");
        User user2 = new User(null, "anh", "anh@example.com");
        this.userRepository.save(user1);
        this.userRepository.save(user2);
        List<User> expectedUsers = List.of(user1, user2);

        // action
        String resultString = mockMvc.perform(
                get("/users"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        // assert
        List<User> resultUsers = objectMapper.readValue(resultString, new TypeReference<List<User>>() {
        });
        assertEquals(expectedUsers.size(), resultUsers.size());

    }

    @Test
    public void getUserById_ShouldReturnUser_WhenIdExists() throws Exception {
        // arrange
        User user1 = new User(null, "tuan", "tuan@example.com");
        User user2 = new User(null, "anh", "anh@example.com");
        User userInput = this.userRepository.saveAndFlush(user1);
        this.userRepository.saveAndFlush(user2);
        // action
        String resultString = mockMvc.perform(
                get("/users/{id}", userInput.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        // assert
        User resultUser = objectMapper.readValue(resultString, User.class);
        assertEquals(userInput.getId(), resultUser.getId());
    }

}
