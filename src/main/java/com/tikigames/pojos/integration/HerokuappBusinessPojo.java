package com.tikigames.pojos.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HerokuappBusinessPojo {

	private String id;
	private String departure;
	private String arrival;

	private Long departureTime;
	private Long arrivalTime;
}
