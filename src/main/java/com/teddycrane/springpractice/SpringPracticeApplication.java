package com.teddycrane.springpractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SpringPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringPracticeApplication.class, args);
	}

}
