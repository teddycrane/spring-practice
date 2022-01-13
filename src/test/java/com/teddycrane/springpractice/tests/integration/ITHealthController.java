package com.teddycrane.springpractice.tests.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ITHealthController extends IntegrationBase {

    @Test
    @DisplayName("Health Check Should Return Healthy")
    public void shouldReturn200_WhenServerIsOnline() throws Exception {
        this.performGet("/health").andExpect(status().isOk());
    }
}
