package com.teddycrane.springpractice.tests.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.teddycrane.springpractice.user.request.AuthenticationRequest;

@Tag("Integration")
@TestInstance(Lifecycle.PER_CLASS)
public class ITUserController extends IntegrationBase {

    @Value("${test.user.username}")
    private String testUsername;

    @Value("${test.user.password}")
    private String testUserPassword;

    @Test
    @DisplayName("Users with User level permissions should be able to authenticate")
    public void authentication_shouldAuthenticateForUsers() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(this.testUsername, null, this.testUserPassword);
        String requestBody = this.gson.toJson(request);
        this.mockMvc.perform(post("/users/login")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value("true"))
                .andExpect(jsonPath("$.token").exists())
                .andReturn();
    }

    @Test
    @DisplayName("Should not get users if the user is not authenticated")
    public void getUsers_shouldNotReturnUsersWithNoToken() throws Exception {
        this.mockMvc.perform(get("/users/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should get all users")
    public void getUsers_shouldReturnAListOfUsers() throws Exception {
        String authToken = this.getUserAuthToken();
        this.mockMvc.perform(get("/users/all")
                .contentType("application/json")
                .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]").exists());
    }
}
