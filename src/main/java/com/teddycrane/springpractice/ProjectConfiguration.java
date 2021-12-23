package com.teddycrane.springpractice;

import com.teddycrane.springpractice.components.AuthorizationInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ProjectConfiguration implements WebMvcConfigurer 
{
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new AuthorizationInterceptor());
    }
}
