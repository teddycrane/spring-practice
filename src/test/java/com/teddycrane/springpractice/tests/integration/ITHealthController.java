package com.teddycrane.springpractice.tests.integration;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ITHealthController extends IntegrationBase {

    @Test
    public void shouldReturn200_WhenServerIsOnline() throws Exception {
        this.performGet("/health").andExpect(status().isOk());
    }
}
