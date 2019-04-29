package com.tikigames.integration.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.tikigames.Messages;
import com.tikigames.exceptions.CustomException;
import com.tikigames.integration.HerokuappProviderIntegration;
import com.tikigames.pojos.integration.HerokuappCheapPojo;

import reactor.core.publisher.Flux;

@Component("cheapFlightProvider")
public class HerokuappCheapFlightProvider extends HerokuappProviderIntegration {

	@Value("${integration.herokuapp.cheap.url}")
	private String cheapUrl;

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
