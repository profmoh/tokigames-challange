package com.tikigames.integration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

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
	protected static ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			System.out.println(clientRequest.method() + "    " + clientRequest.url());

			clientRequest.headers().forEach((name, values) -> values
					.forEach(value -> System.out.println(name + "    " + value)));

			return Mono.just(clientRequest);
		});
	}
}
