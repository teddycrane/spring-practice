package com.teddycrane.springpractice.tests.unittests;

import com.teddycrane.springpractice.health.Health;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HealthTest {

  private Health health;

  @BeforeEach
  public void init() {
    this.health = new Health();
  }

  @Test
  public void health_shouldBeNotNull() {
    Assertions.assertNotNull(this.health);
  }
}
