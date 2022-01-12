package com.teddycrane.springpractice.tests.integration;

import com.teddycrane.springpractice.racer.request.CreateRacerRequest;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ITRacerController extends IntegrationBase {
    @Test
    public void createRacer_shouldReturn200WhenCreated() throws Exception {
        CreateRacerRequest request = new CreateRacerRequest("test", "racer");
        String json = this.gson.toJson(request);

        var result = this.mockMvc.perform(post("/racer/new").accept(MediaType.APPLICATION_JSON).content(json));
        System.out.println(result);
    }
}
