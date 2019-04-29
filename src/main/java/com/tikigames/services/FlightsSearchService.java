package com.tikigames.services;

import java.util.Date;

import com.tikigames.exceptions.CustomException;
import com.tikigames.pojos.AvailableFlightsPojo;

import reactor.core.publisher.Flux;

public interface FlightsSearchService {

	Flux<AvailableFlightsPojo> flightsSearch(
			String departure,
			String arrival,
			Date departDate,
			Date returnDate,
			Double cost,
			Integer pageNumber,
			Integer pageSize,
			String sortBy) throws CustomException;
}
