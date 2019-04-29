package com.tikigames;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

public abstract class Messages {

	/**
	 * Message Object Instances
	 */
	public static final MessageObject UNEXPECTED_ERROR = new MessageObject("UNEXPECTED_ERROR", "Unexpected internal error", null);

	public static final MessageObject TOO_MANY_REQUESTS = new MessageObject("TOO_MANY_REQUESTS", "Too many requests", HttpStatus.TOO_MANY_REQUESTS);

	public static final MessageObject ERROR_RETRIEVING_DATA = new MessageObject("ERROR_RETRIEVING_DATA", "error retrieving data", null);

	/**
	 * Messages Keys
	 */
//	public static final String EXPIRED_OR_INVALIED_KWT_TOKEN = EXPIRED_OR_INVALIED_KWT_TOKEN_OBJECT.getKey();

	/**
	 * 
	 * @author Mohamed
	 * 
	 * Messages Object
	 */
	@Getter
	@AllArgsConstructor
	public static class MessageObject {
		private String key;
		private String message;
		private HttpStatus httpStatus;
	}
}
