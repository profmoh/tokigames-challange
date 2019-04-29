package com.tikigames.services;

import java.util.Date;

import com.tikigames.exceptions.CustomException;
import com.tikigames.pojos.AvailableFlightsPojo;

import reactor.core.publisher.Flux;

public interface FlightsSearchService {

	/**
	 * handle the logic of searching for flights form different providers then filtering, sorting, and paginating the results
	 * 
	 * @param departure filter by departure
	 * @param arrival filter by arrival
	 * @param departureDate filter by departure date, format of input (yyyy-MM-dd)
	 * @param arrivalDate filter by arrival date, format of input (yyyy-MM-dd)
	 * @param pageNumber set the number of page to retrieve in pagination
	 * @param pageSize set the number of records per page in pagination
	 * @param sortBy set the field to sort by (departure, arrival, departureDate, arrivalDate)
	 * @param sortDirection set the sort direction (Asc, desc)
	 * @return
	 * @throws CustomException
	 */
	Flux<AvailableFlightsPojo> flightsSearch(
			String departure,
			String arrival,
			Date departureDate,
			Date arrivalDate,
			Integer pageNumber,
			Integer pageSize,
			String sortBy,
			String sortDirection) throws CustomException;
}
