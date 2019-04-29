package com.tikigames.pojos.integration;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HerokuappCheapPojo {

	private String uuid;
	private String flight;

	private LocalDateTime departure;
	private LocalDateTime arrival;
}
