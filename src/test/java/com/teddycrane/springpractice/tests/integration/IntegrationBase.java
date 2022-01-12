package com.teddycrane.springpractice.tests.integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teddycrane.springpractice.SpringPracticeApplication;
import com.teddycrane.springpractice.user.request.AuthenticationRequest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ActiveProfiles("test")
@SpringBootTest(classes = SpringPracticeApplication.class)
@AutoConfigureMockMvc
class IntegrationBase {

    @Autowired
    protected MockMvc mockMvc;
    protected String authToken = "";

    protected final Gson gson = new Gson();

    // /**
    // * Sets up tests. At this time, adds a single user to allow authorization
    // testing flow through the application
    // */
    // @BeforeAll
    // public void setUp() {

    // }

    /**
     * Gets User-level authenticated auth token.
     * 
     * @return A string in the format "Bearer {{token}}"
     */
    // protected String getUserAuthToken() {
    //     AuthenticationRequest request = new AuthenticationRequest("username", null, "email");
    //     // this.mockMvc.perform(post("/users/login").accept(MediaType.APPLICATION_JSON));
    // }

    protected ResultActions performGet(String url) throws Exception {
        return this.mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));
    }
}
