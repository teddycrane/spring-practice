package com.teddycrane.springpractice.tests.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Value;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import com.teddycrane.springpractice.user.request.AuthenticationRequest;

@TestInstance(Lifecycle.PER_CLASS)
public class ITUserController extends IntegrationBase {

    @Value("${test.user.username}")
    private String testUsername;

    @Value("${test.user.password}")
    private String testUserPassword;

    @Disabled
    @Test
    @DisplayName("Users with User level permissions should be able to authenticate")
    public void authentication_shouldAuthenticateForUsers() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(this.testUsername, null, this.testUserPassword);
        String requestBody = this.gson.toJson(request);
        var result = this.mockMvc.perform(post("/users/login")
                .contentType("application/json")
                .content(requestBody))
                .andReturn();
        System.out.println(result);
    }

    @Test
    @DisplayName("Should get all users")
    public void getUsers_shouldReturnAListOfUsers() throws Exception {
        var result = this.mockMvc.perform(get("/users/all")
                .contentType("application/json")
                .header("Authorization",
                        "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiVGVkZHkgQ3JhbmUiLCJpYXQiOjE2NDE4MzYzNDUsImV4cCI6MjY0MjkzOTk0NSwiaXNzIjoiY29tLnRlZGR5Y3JhbmUuc3ByaW5ncHJhY3RpY2UiLCJzdWIiOiJ0Y3JhbmUiLCJqdGkiOiIxMDIzYzllMS02OTAwLTQ1MjAtYjhlYy03NzUzYTVjZGYxMjAifQ.EuahdHrVca4xTLCB1GxK_L_nUloCKkaW8Pmfmj74bgwO6xmZocvRf05RGQdHj39CtYf9iHhgtDy7tVtsUHrCeg"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
