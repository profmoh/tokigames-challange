package com.tikigames.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tikigames.pojos.AvailableFlightsPojo;
import com.tikigames.services.FlightsSearchService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Flux;

/**
 * 
 * @author Mohamed
 *
 */
@RestController
@Api(tags = "Flights search")
@RequestMapping("${api.path}flights")
public class FlightsSearchController {

	@Autowired
	private FlightsSearchService flightsSearchService;

	/**
	 * End point to search for available flights
	 * 
	 * It support sorting, filtering and pagination using URL params.
	 * 
	 * @param departure the place of departure for filtering - optional -
	 * @param arrival the place of arrival for filtering - optional -
	 * @param departDate the departure date for filtering - optional -
	 * @param returnDate the arrival date for filtering - optional -
	 * @param cost the maximum cost for filtering - optional -
	 * @param pageNumber the page number for pagination - optional -
	 * @param pageSize the number of records per page for pagination - optional -
	 * @param sortBy the name of field for sorting for sorting - optional -
	 * 
	 * @return Flux of all available flights
	 */
	@GetMapping("/search")
	@ApiOperation(value = "${FlightsSearchController.flightsSearch}", response = AvailableFlightsPojo.class)
	public Flux<AvailableFlightsPojo> flightsSearch(
			@RequestParam(name = "departure", required = false) String departure,
			@RequestParam(name = "arrival", required = false) String arrival,
			@RequestParam(name = "departDate", required = false) Date departDate,
			@RequestParam(name = "returnDate", required = false) Date returnDate,
			@RequestParam(name = "cost", required = false) Double cost,
			@RequestParam(name = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = "cost") String sortBy) {

		return flightsSearchService.flightsSearch(departure,
				arrival,
				departDate,
				returnDate,
				cost,
				pageNumber,
				pageSize,
				sortBy);
	}
}
