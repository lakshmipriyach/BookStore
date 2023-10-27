package com.bookstore.exception;

public class NotAcceptableException extends RuntimeException {

	public NotAcceptableException(String message) {
		super(message);

	}

	public NotAcceptableException(String message, Throwable cause) {
		super(message, cause);
	}

}
