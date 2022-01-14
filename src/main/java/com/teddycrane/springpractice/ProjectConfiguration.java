package com.teddycrane.springpractice;

import com.teddycrane.springpractice.auth.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ProjectConfiguration implements WebMvcConfigurer {

  @Autowired private AuthorizationInterceptor authorizationInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(authorizationInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns(
            "/users/login",
            "/error",
            "/users/reset-password",
            "/health",
            "/users/create-new",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/**");
  }
}
