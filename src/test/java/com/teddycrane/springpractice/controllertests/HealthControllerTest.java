package com.teddycrane.springpractice.controllertests;

import com.teddycrane.springpractice.controller.HealthController;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HealthControllerTest {

	@Autowired
	private HealthController healthController;

	@Test
	public void contextLoads() throws Exception {
		assertThat(healthController).isNotNull();
	}
}
