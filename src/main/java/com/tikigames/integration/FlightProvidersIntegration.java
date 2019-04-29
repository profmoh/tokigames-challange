package com.tikigames.integration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.tikigames.pojos.integration.HerokuappPojo;

import reactor.core.publisher.Flux;

@Component
public abstract class FlightProvidersIntegration {

	protected WebClient webClient;

	@Value("${integration.herokuapp.base-url}")
	private String baseUrl;

	@PostConstruct
	public void init() {
		webClient = WebClient.builder()
		        .baseUrl(baseUrl)
		        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
		        .build();
	}

	public abstract <T extends HerokuappPojo> Flux<T> searchAvailableFlights();
}
