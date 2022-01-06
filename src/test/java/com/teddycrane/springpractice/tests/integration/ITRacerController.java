package com.teddycrane.springpractice.tests.integration;

import com.teddycrane.springpractice.racer.RacerController;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ITRacerController extends IntegrationBase {

    @Autowired
    private RacerController racerController;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(racerController);
    }
}
