package com.teddycrane.springpractice.tests.integration;

import com.teddycrane.springpractice.SpringPracticeApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(classes = SpringPracticeApplication.class)
@AutoConfigureMockMvc
class IntegrationBase {

    @Autowired
    protected MockMvc mockMvc;

    protected ResultActions performGet(String url) throws Exception {
        return this.mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));
    }
}
