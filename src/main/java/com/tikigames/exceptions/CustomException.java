package com.tikigames.exceptions;

import org.springframework.http.HttpStatus;

import com.tikigames.Messages;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String key;
	private final String message;

	private final HttpStatus httpStatus;

	public CustomException(Messages.MessageObject message) {
		this.key = message.getKey();
		this.message = message.getMessage();

		this.httpStatus = message.getHttpStatus() == null ? HttpStatus.INTERNAL_SERVER_ERROR : message.getHttpStatus();
	}

	@Override
	public String getMessage() {
		return key;
	}

	public String getInternalMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
