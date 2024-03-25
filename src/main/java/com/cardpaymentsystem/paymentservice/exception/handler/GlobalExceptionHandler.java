package com.cardpaymentsystem.paymentservice.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cardpaymentsystem.paymentservice.exception.ApiCallException;
import com.cardpaymentsystem.paymentservice.exception.NotFoundException;
import com.cardpaymentsystem.paymentservice.exception.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ValidationException.class)
	public ProblemDetail handleValidationException(ValidationException exception) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(NotFoundException.class)
	public ProblemDetail handleNotFoundException(NotFoundException exception) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler(ApiCallException.class)
	public ProblemDetail handleApiCallException(ApiCallException exception) {
		return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(exception.getStatus()), exception.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleException(Exception exception) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
	}
}
