package com.teddycrane.springpractice.tests.integration;

import com.teddycrane.springpractice.racer.RacerController;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ITRacerController extends IntegrationBase {

    @Autowired
    private RacerController racerController;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(racerController);
    }

    @Test
    public void shouldReturn200_WhenRacerIsCreated() throws Exception {
        this.mockMvc.perform(post("/racer/new")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
