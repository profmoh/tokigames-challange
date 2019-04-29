package com.tikigames;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class TokiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TokiApplication.class, args);
	}

}
