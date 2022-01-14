package com.teddycrane.springpractice.tests.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.teddycrane.springpractice.racer.request.CreateRacerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class ITRacerController extends IntegrationBase {

  private String authToken;

  @BeforeEach
  public void authenticate() throws Exception {
    this.authToken = this.getUserAuthToken();
  }

  @Test
  public void createRacer_shouldReturn200WhenCreated() throws Exception {
    String firstName, lastName;
    firstName = this.faker.name().firstName();
    lastName = this.faker.name().lastName();
    CreateRacerRequest request = new CreateRacerRequest(firstName, lastName);
    String json = this.gson.toJson(request);

    this.mockMvc
        .perform(
            post("/racer/new")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authToken)
                .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.firstName").value(firstName))
        .andExpect(jsonPath("$.lastName").value(lastName));
  }
}
