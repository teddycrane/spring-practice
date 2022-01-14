package com.teddycrane.springpractice.tests.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

@Tag("Integration")
public class ITEventController extends IntegrationBase {
    private final String testEventId = "02fd6b48-33f0-4bdc-af52-ba4bbd802db7";

    @Test
    @DisplayName("Get All Events should return valid data")
    public void getAllEvents_shouldGetValidResult() throws Exception {
        this.mockMvc.perform(get("/events/all")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", userAuthToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Get Event should return valid data")
    public void getEvent_shouldReturnData() throws Exception {
        String url = String.format("/events/%s", testEventId);
        this.mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", userAuthToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(testEventId));
    }

    @Test
    public void getEvent_shouldFailForEventNotInDatabase() throws Exception {
        String url = String.format("/events/%s", UUID.randomUUID());

        this.mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", userAuthToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createEvent_shouldCreateEvent() throws Exception {
        String content = "{\"name\": \"test name2\",\"startDate\": \"2021-11-06T19:58:43.933+00:00\",\"endDate\": \"2021-11-07T19:58:43.933+00:00\"}";
        this.mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", userAuthToken)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("test name2"));
    }

    @Test
    public void createEvent_shouldFailWithNoRequestBody() throws Exception {
        this.mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", userAuthToken)
                .content(""))
                .andExpect(status().isBadRequest());
    }

}
