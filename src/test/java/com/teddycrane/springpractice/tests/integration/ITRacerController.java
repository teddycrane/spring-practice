package com.teddycrane.springpractice.tests.integration;

import com.teddycrane.springpractice.SpringPracticeApplication;
import com.teddycrane.springpractice.racer.RacerController;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SpringPracticeApplication.class)
public class ITRacerController {

    @Autowired
    private RacerController racerController;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(racerController);
    }
}
