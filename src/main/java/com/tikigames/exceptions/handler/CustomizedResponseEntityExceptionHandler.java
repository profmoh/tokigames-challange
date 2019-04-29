package com.tikigames.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.datazord.pojos.ResponseDetails;
import com.tikigames.Messages;
import com.tikigames.exceptions.CustomException;
import com.weddini.throttling.ThrottlingException;

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private final ResponseEntity<Object> buildResponseEntity(HttpStatus httpStatus, Object body) {
		if (httpStatus == null)
			httpStatus = HttpStatus.OK;

		if (body == null)
			return ResponseEntity.status(httpStatus).build();
		else
			return ResponseEntity.status(httpStatus).body(body);
	}

	@ExceptionHandler(CustomException.class)
	public final ResponseEntity<Object> handleCustomException(CustomException ex, WebRequest request) {
		ex.printStackTrace();
		System.out.println(ex.getInternalMessage());

		ResponseDetails<String> responseDetails =
				ResponseDetails.status(ex.getHttpStatus()).build(ex.getMessage());

		return buildResponseEntity(ex.getHttpStatus(), responseDetails.getBody());
	}

	@ExceptionHandler(ThrottlingException.class)
	public final ResponseEntity<Object> handleUserNotFoundException(ThrottlingException ex) {
		ex.printStackTrace();

		ResponseDetails<String> responseDetails =
				ResponseDetails
				.status(Messages.TOO_MANY_REQUESTS.getHttpStatus())
				.build(Messages.TOO_MANY_REQUESTS.getKey());

		return buildResponseEntity(responseDetails.getHttpStatus(), responseDetails.getBody());
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		ex.printStackTrace();

		ResponseDetails<String> responseDetails = ResponseDetails.status(HttpStatus.INTERNAL_SERVER_ERROR).build(ex.getMessage());

		return buildResponseEntity(null, responseDetails.getBody());
	}
}
