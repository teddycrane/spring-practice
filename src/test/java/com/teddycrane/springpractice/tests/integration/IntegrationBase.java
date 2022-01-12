package com.teddycrane.springpractice.tests.integration;

import com.google.gson.Gson;
import com.teddycrane.springpractice.SpringPracticeApplication;
import com.teddycrane.springpractice.user.request.AuthenticationRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@SpringBootTest(classes = SpringPracticeApplication.class)
// @AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/import.sql")
@AutoConfigureMockMvc
class IntegrationBase {

    @Autowired
    protected MockMvc mockMvc;

    @Value("${test.user.username}")
    private String testUsername;

    @Value("${test.user.password}")
    private String testUserPassword;

    protected final Gson gson = new Gson();

    /**
     * Makes a request to get an authentication token.
     *
     * @return A string formatted as "Bearer {token}".
     * @throws Exception Throws an exception if an error occurs.
     */
    protected String getUserAuthToken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(testUsername, null, testUserPassword);
        String requestBody = this.gson.toJson(request);

        MvcResult response = this.mockMvc.perform(post("/users/login")
                .contentType("application/json")
                .content(requestBody)).andReturn();
        String result = response.getResponse().getContentAsString();
        System.out.printf("result {}", result);
        return String.format("Bearer {}", result);
    }

    protected ResultActions performGet(String url) throws Exception {
        return this.mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));
    }
}
