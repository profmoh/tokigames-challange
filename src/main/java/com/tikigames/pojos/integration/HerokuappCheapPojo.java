package com.tikigames.pojos.integration;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HerokuappCheapPojo extends HerokuappPojo {

	private Date departDateTime;
	private Date returnDateTime;

	public HerokuappCheapPojo(String departure, String arrival, Double cost, Date departDateTime, Date returnDateTime) {
		super(departure, arrival, cost);

		this.departDateTime = departDateTime;
		this.returnDateTime = returnDateTime;
	}
}
