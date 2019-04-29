package com.tikigames.pojos.integration;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class HerokuappBusinessPojo extends HerokuappPojo {

	@Value("${config.dateTimeFormate}")
	private String defaultDateTimeFormate;

	private String departReturnDateTime;

	private Date departDateTime;
	private Date returnDateTime;

	public HerokuappBusinessPojo(String departure, String arrival, Double cost, String departReturnDateTime) {
		super(departure, arrival, cost);

		setDepartReturnDateTime(departReturnDateTime);
	}

	public String getDepartReturnDateTime() {
		return departReturnDateTime;
	}

	public void setDepartReturnDateTime(String departReturnDateTime) {
		if(departReturnDateTime == null) {
			this.departDateTime = null;
			this.returnDateTime = null;
			this.departReturnDateTime = null;

			return;
		}

		this.departReturnDateTime = departReturnDateTime;

		String[] departReturnDateTimeParts =
				this.departReturnDateTime.split("\\s*-\\s*");

		SimpleDateFormat dateFormate = new SimpleDateFormat(defaultDateTimeFormate);

		if(departReturnDateTimeParts.length > 0 && StringUtils.isNoneBlank(departReturnDateTimeParts[0]))
			try {
				this.departDateTime = dateFormate.parse (departReturnDateTimeParts[0]);
			} catch (Exception e) {
			}

		if(departReturnDateTimeParts.length > 1 && StringUtils.isNoneBlank(departReturnDateTimeParts[1]))
			try {
				this.returnDateTime = dateFormate.parse (departReturnDateTimeParts[1]);
			} catch (Exception e) {
			}
	}

	public Date getDepartDateTime() {
		return departDateTime;
	}

	public Date getReturnDateTime() {
		return returnDateTime;
	}
}
