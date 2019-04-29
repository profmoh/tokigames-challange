package com.tikigames.services.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.datazord.DateTimeUtils;
import com.tikigames.Messages;
import com.tikigames.exceptions.CustomException;
import com.tikigames.integration.FlightProvidersIntegration;
import com.tikigames.pojos.AvailableFlightsPojo;
import com.tikigames.pojos.integration.HerokuappBusinessPojo;
import com.tikigames.pojos.integration.HerokuappCheapPojo;
import com.tikigames.services.FlightsSearchService;

import reactor.core.publisher.Flux;

@Service
public class FlightsSearchServiceImpl implements FlightsSearchService {

	@Autowired
	@Qualifier("cheapFlightProvider")
	private FlightProvidersIntegration cheapFlightProvider;

	@Autowired
	@Qualifier("businessFlightProvider")
	private FlightProvidersIntegration businessFlightProvider;

	@Value("${config.dateTimeFormate}")
	private String defaultDateTimeFormate;

	public Flux<AvailableFlightsPojo> flightsSearch(String departure,
			String arrival,
			Date departDate,
			Date returnDate,
			Double cost,
			String sortBy) throws CustomException {

		CompletableFuture <Flux<AvailableFlightsPojo>> cheapProviderFlightsCompletable =
				getCheapFlights(departure, arrival, departDate, returnDate, cost);

		CompletableFuture <Flux<AvailableFlightsPojo>> businessProviderFlightsCompletable =
				getBusinessFlights(departure, arrival, departDate, returnDate, cost);

		CompletableFuture.allOf(cheapProviderFlightsCompletable, businessProviderFlightsCompletable).join();

		Flux<AvailableFlightsPojo> results;
		try {
			results = cheapProviderFlightsCompletable.get().mergeWith(businessProviderFlightsCompletable.get());
		} catch (InterruptedException | ExecutionException e) {
			throw new CustomException(Messages.ERROR_RETRIEVING_DATA);
		}

		if(StringUtils.isBlank(sortBy))
			return results;

		switch (sortBy.toLowerCase()) {
		case "departure":
			results.sort(Comparator.comparing(AvailableFlightsPojo::getDeparture));
		case "arrival":
			results.sort(Comparator.comparing(AvailableFlightsPojo::getArrival));
		case "departDate":
			results.sort(Comparator.comparing(AvailableFlightsPojo::getDepartDateTime));
		case "returnDate":
			results.sort(Comparator.comparing(AvailableFlightsPojo::getReturnDateTime));
		case "cost":
			results.sort(Comparator.comparing(AvailableFlightsPojo::getCost));
		default:
			return results;
		}
	}

	@Async("taskExecuter")
	private CompletableFuture<Flux<AvailableFlightsPojo>> getCheapFlights(String departure, String arrival, Date departDate,
			Date returnDate, Double cost) {
		DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();

		Flux<HerokuappCheapPojo> cheapProviderFlights = cheapFlightProvider.searchAvailableFlights();

		Flux<AvailableFlightsPojo> cheapFlights = cheapProviderFlights
				.filter(c -> StringUtils.isBlank(departure) || c.getDeparture().equalsIgnoreCase(departure))
				.filter(c -> StringUtils.isBlank(arrival) || c.getArrival().equalsIgnoreCase(arrival))
				.filter(c -> departDate == null || dateTimeComparator.compare(c.getDepartDateTime(), departDate) == 0)
				.filter(c -> returnDate == null || dateTimeComparator.compare(c.getReturnDateTime(), returnDate) == 0)
				.filter(c -> cost == null || c.getCost().compareTo(cost) <= 0)
				.map(c -> new AvailableFlightsPojo(c.getDeparture(), c.getArrival(),
						DateTimeUtils.formateDate(c.getDepartDateTime(), defaultDateTimeFormate),
						DateTimeUtils.formateDate(c.getDepartDateTime(), defaultDateTimeFormate), c.getCost()));

		return CompletableFuture.completedFuture(cheapFlights);
	}

	@Async("taskExecuter")
	private CompletableFuture<Flux<AvailableFlightsPojo>> getBusinessFlights(String departure, String arrival, Date departDate,
			Date returnDate, Double cost) {
		DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();

		Flux<HerokuappBusinessPojo> businessProviderFlights = businessFlightProvider.searchAvailableFlights();

		Flux<AvailableFlightsPojo> cheapFlights = businessProviderFlights
				.filter(c -> StringUtils.isBlank(departure) || c.getDeparture().equalsIgnoreCase(departure))
				.filter(c -> StringUtils.isBlank(arrival) || c.getArrival().equalsIgnoreCase(arrival))
				.filter(c -> c.getDepartDateTime() != null
							&& (departDate == null || dateTimeComparator.compare(c.getDepartDateTime(), departDate) == 0))
				.filter(c -> c.getReturnDateTime() != null
						&& (returnDate == null || dateTimeComparator.compare(c.getReturnDateTime(), returnDate) == 0))
				.filter(c -> cost == null || c.getCost().compareTo(cost) <= 0)
				.map(c -> new AvailableFlightsPojo(c.getDeparture(), c.getArrival(),
						DateTimeUtils.formateDate(c.getDepartDateTime(), defaultDateTimeFormate),
						DateTimeUtils.formateDate(c.getDepartDateTime(), defaultDateTimeFormate), c.getCost()));

		return CompletableFuture.completedFuture(cheapFlights);
	}
}
