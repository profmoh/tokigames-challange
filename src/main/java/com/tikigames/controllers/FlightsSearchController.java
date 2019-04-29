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

@RestController
@Api(tags = "Flights search")
@RequestMapping("${api.path}flights")
public class FlightsSearchController {

	@Autowired
	private FlightsSearchService flightsSearchService;

	@GetMapping("/search")
	@ApiOperation(value = "${FlightsSearchController.flightsSearch}", response = AvailableFlightsPojo.class)
	public Flux<AvailableFlightsPojo> flightsSearch(
			@RequestParam(name = "departure", required = false) String departure,
			@RequestParam(name = "arrival", required = false) String arrival,
			@RequestParam(name = "departDate", required = false) Date departDate,
			@RequestParam(name = "returnDate", required = false) Date returnDate,
			@RequestParam(name = "cost", required = false) Double cost,
			@RequestParam(name = "sortBy", required = false) String sortBy) {

		return flightsSearchService.flightsSearch(departure,
				arrival,
				departDate,
				returnDate,
				cost,
				sortBy);
	}
}
