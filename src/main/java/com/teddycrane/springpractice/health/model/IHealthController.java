package com.teddycrane.springpractice.health.model;

import com.teddycrane.springpractice.health.Health;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/health")
public interface IHealthController {
  @Operation(summary = "Get the health status of the server.")
  @ApiResponse(responseCode = "200", description = "Server status is healthy")
  @GetMapping
  Health getHealth();
}
