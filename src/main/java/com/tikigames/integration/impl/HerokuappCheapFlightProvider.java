package com.tikigames.integration.impl;

import java.time.Duration;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.tikigames.Messages;
import com.tikigames.exceptions.CustomException;
import com.tikigames.integration.HerokuappProviderIntegration;
import com.tikigames.pojos.integration.HerokuappCheapPojo;

import reactor.core.publisher.Flux;
import reactor.ipc.netty.http.client.HttpClientOptions;

@Component("cheapFlightProvider")
public class HerokuappCheapFlightProvider extends HerokuappProviderIntegration {

	private WebClient webClient;

	@Value("${integration.herokuapp.base-url}")
	private String baseUrl;

	@Value("${integration.herokuapp.cheap.url}")
	private String cheapUrl;

	/**
	 * This method will be called once, at the initialization step of any child class for this abstract class.
	 */
	@PostConstruct
	public void init() {
		this.webClient = WebClient
				.builder()
				.baseUrl(baseUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.filter(logRequest())
				.build();
	}

	@Override
	public Flux<HerokuappCheapPojo> searchAvailableFlights() {
		return webClient
				.get()
				.uri(cheapUrl)
				.exchange()
				.timeout(Duration.ofSeconds(60))
				.onErrorMap(e -> {
					throw new CustomException(Messages.ERROR_RETRIEVING_DATA);
				})
				.flatMapMany(clientResponse -> {
					if (!clientResponse.statusCode().equals(HttpStatus.OK))
						throw new CustomException(Messages.ERROR_RETRIEVING_DATA);
		
					return clientResponse.bodyToFlux(HerokuappCheapPojo.class);
				});
	}
}
