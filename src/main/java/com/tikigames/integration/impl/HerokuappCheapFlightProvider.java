package com.tikigames.integration.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.tikigames.Messages;
import com.tikigames.exceptions.CustomException;
import com.tikigames.integration.FlightProvidersIntegration;
import com.tikigames.pojos.integration.HerokuappCheapPojo;

import reactor.core.publisher.Flux;

@Component("cheapFlightProvider")
public class HerokuappCheapFlightProvider implements FlightProvidersIntegration {

	private WebClient webClient;

	@Value("${integration.herokuapp.base-url}")
	private String baseUrl;

	@Value("${integration.herokuapp.cheap.url}")
	private String cheapUrl;

	@PostConstruct
	public void init() {
		webClient = WebClient.builder()
		        .baseUrl(baseUrl)
		        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
		        .build();
	}

	@Override
	public Flux<HerokuappCheapPojo> searchAvailableFlights() {
		return webClient.get()
	            .uri(cheapUrl)
	            .exchange()
	            .onErrorMap(e -> {
	            	throw new CustomException(Messages.ERROR_RETRIEVING_DATA);
	            })
	            .flatMapMany(clientResponse -> {
	            	if(! clientResponse.statusCode().equals(HttpStatus.OK))
	            		throw new CustomException(Messages.ERROR_RETRIEVING_DATA);

	            	return clientResponse.bodyToFlux(HerokuappCheapPojo.class);
	            });
	}
}
