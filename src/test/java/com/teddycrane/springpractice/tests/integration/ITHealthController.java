package com.teddycrane.springpractice.tests.integration;

import com.teddycrane.springpractice.health.HealthController;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = { HealthController.class })
public class ITHealthController {
    @Autowired
    private HealthController healthController;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(healthController);
    }
}
