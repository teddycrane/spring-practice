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

import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.user.request.AuthenticationRequest;
import com.teddycrane.springpractice.user.request.CreateUserRequest;

@Tag("Integration")
@TestInstance(Lifecycle.PER_CLASS)
public class ITUserController extends IntegrationBase {

    @Value("${test.user.username}")
    private String testUsername;

    @Value("${test.user.password}")
    private String testUserPassword;

    private final String editableUserId = "7825ff10-d79e-494c-bfc4-a0184ae7badf";

    // @BeforeEach
    // public void setUp() throws Exception {
    // try {
    // this.userAuthToken = this.getUserAuthToken();
    // } catch (Exception e) {
    // System.out.println("Unhandled Exception thrown while getting authentication
    // token");
    // throw new Exception("Fatal Error");
    // }
    // }

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
        this.mockMvc.perform(get("/users/all")
                .contentType("application/json")
                .header("Authorization", this.userAuthToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]").exists());
    }

    @Test
    @DisplayName("Should get a single user by user id")
    public void getUser_shouldGetUserById() throws Exception {
        this.mockMvc.perform(get(String.format("/users/%s", editableUserId))
                .header("Authorization", userAuthToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(editableUserId))
                .andExpect(jsonPath("$.firstName").value("Test"));
    }

    @Test
    @DisplayName("Should create a user with authentication")
    public void createUser_shouldCreateUserWithoutAuth() throws Exception {
        String username = faker.name().username();
        String password = faker.bothify("????????");
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.bothify("??????@email.fake");
        UserType type = UserType.USER;

        CreateUserRequest request = new CreateUserRequest(username, password, firstName, lastName, email, type);
        String requestBody = this.gson.toJson(request);
        this.mockMvc.perform(post("/users/create-new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @DisplayName("Should allow non-admin users to update their own user profile")
    public void updateUser_shouldAllowSelfUpdate() throws Exception {
        String userId = "1023c9e1-6900-4520-b8ec-7753a5cdf120";
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        String content = String.format("{\"userId\": \"%s\",\"firstName\":\"%s\",\"lastName\":\"%s\"}", userId,
                firstName, lastName);

        this.mockMvc.perform(patch("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("Authorization", userAuthToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should not allow non Admin or Root users to update other users")
    public void updateUser_shouldNotAllowBasicUserToAccess() throws Exception {
        String userId = "5e0c215b-4309-4902-97cf-01e7fc2a17b1";
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        String content = String.format("{\"userId\": \"%s\",\"firstName\":\"%s\",\"lastName\":\"%s\"}", userId,
                firstName, lastName);

        this.mockMvc.perform(patch("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", userAuthToken)
                .content(content))
                .andExpect(status().is4xxClientError());
    }
}
