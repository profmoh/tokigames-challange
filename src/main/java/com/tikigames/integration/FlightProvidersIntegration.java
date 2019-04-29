package com.tikigames.integration;

import com.tikigames.pojos.integration.HerokuappPojo;

import reactor.core.publisher.Flux;

public interface FlightProvidersIntegration {

	<T extends HerokuappPojo> Flux<T> searchAvailableFlights();
}
