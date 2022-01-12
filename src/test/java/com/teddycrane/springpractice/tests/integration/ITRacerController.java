package com.teddycrane.springpractice.tests.integration;

import com.teddycrane.springpractice.racer.request.CreateRacerRequest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Disabled
@TestInstance(Lifecycle.PER_CLASS)
public class ITRacerController extends IntegrationBase {

    private String authToken;

    @BeforeAll
    public void authenticate() throws Exception {
        this.authToken = this.getUserAuthToken();
    }

    @Test
    public void createRacer_shouldReturn200WhenCreated() throws Exception {
        CreateRacerRequest request = new CreateRacerRequest("test", "racer");
        String json = this.gson.toJson(request);

        var result = this.mockMvc.perform(post("/racer/new")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", authToken)
                .content(json));
        System.out.println(result);
    }
}
