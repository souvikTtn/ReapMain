package com.Reap.ReapProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication
public class ReapProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReapProjectApplication.class, args);
		System.out.println("Reap Started");
	}
}
