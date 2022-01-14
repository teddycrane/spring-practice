package com.teddycrane.springpractice.tests.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ITHealthController extends IntegrationBase {

  @Test
  @DisplayName("Health Check Should Return Healthy")
  public void shouldReturn200_WhenServerIsOnline() throws Exception {
    this.performGet("/health").andExpect(status().isOk());
  }
}
