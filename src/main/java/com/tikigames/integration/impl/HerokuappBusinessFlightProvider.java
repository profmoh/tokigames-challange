package com.tikigames.integration.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.tikigames.Messages;
import com.tikigames.exceptions.CustomException;
import com.tikigames.integration.HerokuappProviderIntegration;
import com.tikigames.pojos.integration.HerokuappBusinessPojo;

import reactor.core.publisher.Flux;

@Component("businessFlightProvider")
public class HerokuappBusinessFlightProvider extends HerokuappProviderIntegration {

	@Value("${integration.herokuapp.business.url}")
	private String businessUrl;

	@Override
	public Flux<HerokuappBusinessPojo> searchAvailableFlights() {
		return webClient.get()
	            .uri(businessUrl)
	            .exchange()
	            .onErrorMap(e -> {
	            	throw new CustomException(Messages.ERROR_RETRIEVING_DATA);
	            })
	            .flatMapMany(clientResponse -> {
	            	if(! clientResponse.statusCode().equals(HttpStatus.OK))
	            		throw new CustomException(Messages.ERROR_RETRIEVING_DATA);

	            	return clientResponse.bodyToFlux(HerokuappBusinessPojo.class);
	            });
	}
}
