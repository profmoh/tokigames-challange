package com.tikigames.integration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * An a for all Flight Providers.
 * 
 * It initialize the WebClient, and define the signature for searchAvailableFlights method.
 * 
 */
/* note: I may make change it to interface or change the design depending on the requirement. */
@Component
public abstract class HerokuappProviderIntegration {

	protected WebClient webClient;

	@Value("${integration.herokuapp.base-url}")
	private String baseUrl;

	/**
	 * This method will be called once, at the initialization step of any child class for this abstract class.
	 */
	@PostConstruct
	public void init() {
		this.webClient = WebClient.builder()
		        .baseUrl(baseUrl)
		        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
		        .filter(logRequest())
		        .build();
	}

	/**
	 * The method used to call the HerokuappPojo providers APIs and return the result flights as FLUX.
	 * 
	 * @param <T>
	 * @return Flux<T> where (T extends HerokuappPojo) of all available flights from HerokuappPojo providers.
	 */
	public abstract <T> Flux<T> searchAvailableFlights();

	/**
	 * log request url and headers to console
	 * 
	 * @return
	 */
	private static ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			System.out.println(clientRequest.method() + "    " + clientRequest.url());

			clientRequest.headers().forEach((name, values) -> values
					.forEach(value -> System.out.println(name + "    " + value)));

			return Mono.just(clientRequest);
		});
	}
}
