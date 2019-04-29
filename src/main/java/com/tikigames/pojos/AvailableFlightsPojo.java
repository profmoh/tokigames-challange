package com.tikigames.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AvailableFlightsPojo {

	String id;

	String departure;
	String arrival;

	String departureDateTime;
	String arrivalDateTime;
}
