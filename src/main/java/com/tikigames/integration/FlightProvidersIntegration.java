package com.tikigames.integration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.tikigames.pojos.integration.HerokuappPojo;

import reactor.core.publisher.Flux;

/**
 * An a for all Flight Providers.
 * 
 * It initialize the WebClient, and define the signature for searchAvailableFlights method.
 * 
 */
/* note: I may make change it to interface or change the design depending on the requirement. */
@Component
public abstract class FlightProvidersIntegration {

	protected WebClient webClient;

	@Value("${integration.herokuapp.base-url}")
	private String baseUrl;

	/**
	 * This method will be called once, at the initialization step of any child class for this abstract class.
	 */
	@PostConstruct
	public void init() {
		webClient = WebClient.builder()
		        .baseUrl(baseUrl)
		        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
		        .build();
	}

	/**
	 * The method used to call the HerokuappPojo providers APIs and return the result flights as FLUX.
	 * 
	 * @param <T> where (T extends HerokuappPojo)
	 * @return Flux<T> where (T extends HerokuappPojo) of all available flights from HerokuappPojo providers.
	 */
	public abstract <T extends HerokuappPojo> Flux<T> searchAvailableFlights();
}
