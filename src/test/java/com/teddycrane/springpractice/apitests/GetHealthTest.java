package com.teddycrane.springpractice.apitests;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetHealthTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testTemplate;

	@Test
	public void healthShouldReturnHealthy() throws Exception {
		assertThat(this.testTemplate.getForObject("http://localhost:" + port + "/health", String.class))
				.contains("Healthy");
	}
}
