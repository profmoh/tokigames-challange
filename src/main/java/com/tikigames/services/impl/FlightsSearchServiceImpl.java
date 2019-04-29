package com.tikigames.services.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.datazord.DateTimeUtils;
import com.tikigames.Messages;
import com.tikigames.comparators.AvailableFlightsPojoSortByArrivalDate;
import com.tikigames.comparators.AvailableFlightsPojoSortByDepartureDate;
import com.tikigames.exceptions.CustomException;
import com.tikigames.integration.HerokuappProviderIntegration;
import com.tikigames.pojos.AvailableFlightsPojo;
import com.tikigames.pojos.integration.HerokuappBusinessPojo;
import com.tikigames.pojos.integration.HerokuappCheapPojo;
import com.tikigames.services.FlightsSearchService;

import reactor.core.publisher.Flux;

@Service
public class FlightsSearchServiceImpl implements FlightsSearchService {

	@Autowired
	@Qualifier("cheapFlightProvider")
	private HerokuappProviderIntegration cheapFlightProvider;

	@Autowired
	@Qualifier("businessFlightProvider")
	private HerokuappProviderIntegration businessFlightProvider;

	@Value("${config.dateTimeFormate}")
	private String defaultDateTimeFormate;

	@Override
	public Flux<AvailableFlightsPojo> flightsSearch(String departure,
			String arrival,
			Date departureDate,
			Date arrivalDate,
			Integer pageNumber,
			Integer pageSize,
			String sortBy,
			String sortDirection) throws CustomException {

		CompletableFuture <Flux<AvailableFlightsPojo>> cheapProviderFlightsCompletable =
				getCheapFlights(departure, arrival, departureDate, arrivalDate);

		CompletableFuture <Flux<AvailableFlightsPojo>> businessProviderFlightsCompletable =
				getBusinessFlights(departure, arrival, departureDate, arrivalDate);

		CompletableFuture.allOf(cheapProviderFlightsCompletable, businessProviderFlightsCompletable).join();

		Flux<AvailableFlightsPojo> results;
		try {
			results = cheapProviderFlightsCompletable.get().mergeWith(businessProviderFlightsCompletable.get());
		} catch (InterruptedException | ExecutionException e) {
			throw new CustomException(Messages.ERROR_RETRIEVING_DATA);
		}

		if(pageNumber != null && pageNumber > 0 && pageSize != null) {
			// pagination
			results =
					Flux.fromStream(
							results
							.toStream()
							.skip(pageSize * (pageNumber - 1))
							.limit(pageSize)
						);
		}

		if(StringUtils.isBlank(sortBy))
			return results;

		boolean sortAsc = true;

		if(StringUtils.isNotBlank(sortDirection) && sortDirection.equalsIgnoreCase("desc"))
			sortAsc = false;

		switch (sortBy.toLowerCase()) {
		case "departure":
			if(sortAsc)
				return results.sort(Comparator.comparing(AvailableFlightsPojo::getDeparture));

			return results.sort(Comparator.comparing(AvailableFlightsPojo::getDeparture).reversed());
		case "arrival":
			if(sortAsc)
				return results.sort(Comparator.comparing(AvailableFlightsPojo::getArrival));

			return results.sort(Comparator.comparing(AvailableFlightsPojo::getArrival).reversed());
		case "departureDate":
			return results.sort(new AvailableFlightsPojoSortByDepartureDate(sortAsc));
		case "arrivalDate":
			return results.sort(new AvailableFlightsPojoSortByArrivalDate(sortAsc));
		default:
			return results;
		}
	}

	/**
	 * search flights for cheap provider and return flux of AvailableFlightsPojo
	 * 
	 * method run in separate thread
	 */
	@Async("taskExecuter")
	private CompletableFuture<Flux<AvailableFlightsPojo>> getCheapFlights(
			String departure, String arrival, Date departureDate, Date arrivalDate) {

		Flux<HerokuappCheapPojo> cheapProviderFlights = cheapFlightProvider.searchAvailableFlights();

		Flux<AvailableFlightsPojo> cheapFlights = cheapProviderFlights
				.filter(c -> filterCheapProviderFlights(c, departure, arrival, departureDate, arrivalDate))
				.map(c -> new AvailableFlightsPojo(
						c.getUuid(),
						c.getFlight().split("\\s*-\\>\\s*")[0],
						c.getFlight().split("\\s*-\\>\\s*")[1],
						DateTimeUtils.formateLocalDateTime(c.getDeparture(), defaultDateTimeFormate),
						DateTimeUtils.formateLocalDateTime(c.getArrival(), defaultDateTimeFormate)
						));

		return CompletableFuture.completedFuture(cheapFlights);
	}

	/**
	 * apply filters to Cheap Provider Flights
	 * 
	 * @param c
	 * @param departure
	 * @param arrival
	 * @param departureDate
	 * @param arrivalDate
	 * @return
	 */
	private boolean filterCheapProviderFlights(HerokuappCheapPojo c,
			String departure, String arrival, Date departureDate, Date arrivalDate) {

		String cDepartureArrival[] = c.getFlight().split("\\s*-\\>\\s*");

		if(cDepartureArrival.length != 2)
			return false;

		String cDeparture = cDepartureArrival[0];
		String cArrival = cDepartureArrival[1];

		if(StringUtils.isNotBlank(departure) && ! cDeparture.equalsIgnoreCase(departure))
			return false;

		if(StringUtils.isNotBlank(arrival) && ! cArrival.equalsIgnoreCase(arrival))
			return false;

		if(departureDate != null
				&& c.getDeparture().toLocalDate().compareTo(DateTimeUtils.convertDateToLocalDateTime(departureDate).toLocalDate()) != 0)
			return false;

		if(arrivalDate != null
				&& c.getArrival().toLocalDate().compareTo(DateTimeUtils.convertDateToLocalDateTime(arrivalDate).toLocalDate()) != 0)
			return false;

		return true;
	}

	/**
	 * search flights for business provider and return flux of AvailableFlightsPojo
	 * 
	 * method run in separate thread
	 */
	@Async("taskExecuter")
	private CompletableFuture<Flux<AvailableFlightsPojo>> getBusinessFlights(
			String departure, String arrival, Date departureDate, Date arrivalDate) {

		DateTimeFormatter formatter =
				DateTimeFormatter.ofPattern(defaultDateTimeFormate).withZone(ZoneId.systemDefault());

		Flux<HerokuappBusinessPojo> businessProviderFlights = businessFlightProvider.searchAvailableFlights();

		Flux<AvailableFlightsPojo> cheapFlights = businessProviderFlights
				.filter(c -> filterBusinessProviderFlights(c, departure, arrival, departureDate, arrivalDate))
				.map(c -> new AvailableFlightsPojo(
						c.getId(),
						c.getDeparture(),
						c.getArrival(),
						formatter.format(Instant.ofEpochMilli(c.getDepartureTime())),
						formatter.format(Instant.ofEpochMilli(c.getArrivalTime()))
						));

		return CompletableFuture.completedFuture(cheapFlights);
	}

	/**
	 * apply filters to Business Provider Flights
	 * 
	 * @param c
	 * @param departure
	 * @param arrival
	 * @param departureDate
	 * @param arrivalDate
	 * @return
	 */
	private boolean filterBusinessProviderFlights(HerokuappBusinessPojo c,
			String departure, String arrival, Date departureDate, Date arrivalDate) {

		LocalDateTime cDepartureDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(c.getDepartureTime()), ZoneId.systemDefault());
		LocalDateTime cArrivalDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(c.getDepartureTime()), ZoneId.systemDefault());

		if(StringUtils.isNotBlank(departure) && ! c.getDeparture().equalsIgnoreCase(departure))
			return false;

		if(StringUtils.isNotBlank(arrival) && ! c.getArrival().equalsIgnoreCase(arrival))
			return false;

		if(departureDate != null
				&& cDepartureDate.toLocalDate().compareTo(DateTimeUtils.convertDateToLocalDateTime(departureDate).toLocalDate()) != 0)
			return false;

		if(arrivalDate != null
				&& cArrivalDate.toLocalDate().compareTo(DateTimeUtils.convertDateToLocalDateTime(arrivalDate).toLocalDate()) != 0)
			return false;

		return true;
	}
}
