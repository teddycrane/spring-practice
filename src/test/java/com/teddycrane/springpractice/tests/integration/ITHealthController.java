package com.teddycrane.springpractice.tests.integration;

import com.teddycrane.springpractice.health.HealthController;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
public class ITHealthController extends IntegrationBase {
    @Autowired
    private HealthController healthController;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(healthController);
    }

    @Test
    public void shouldReturn200_WhenServerIsOnline() throws Exception {
        this.performGet("/health").andExpect(status().isOk());
    }
}
