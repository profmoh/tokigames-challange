package com.tikigames.comparators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import com.tikigames.pojos.AvailableFlightsPojo;

public class AvailableFlightsPojoSortByDepartureDate implements Comparator<AvailableFlightsPojo> {

	@Value("${config.dateTimeFormate}")
	private String defaultDateTimeFormate;

	boolean sortAsc = true;

	public AvailableFlightsPojoSortByDepartureDate(boolean sortAsc) {
		super();

		this.sortAsc = sortAsc;
	}

	@Override
	public int compare(AvailableFlightsPojo af1, AvailableFlightsPojo af2) {
		SimpleDateFormat format = new SimpleDateFormat(defaultDateTimeFormate);

		Date af1Date;
		Date af2Date;

		try {
			af1Date = format.parse(af1.getDepartureDateTime());
			af2Date = format.parse(af2.getDepartureDateTime());
		} catch (ParseException e) {
			return 0;
		}

		if (sortAsc)
			return af1Date.compareTo(af2Date);
		else
			return af2Date.compareTo(af1Date);
	}

}
