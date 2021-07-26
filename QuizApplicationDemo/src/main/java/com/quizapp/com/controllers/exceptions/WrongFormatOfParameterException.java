package com.quizapp.com.controllers.exceptions;

public class WrongFormatOfParameterException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public WrongFormatOfParameterException() {
	}

	public WrongFormatOfParameterException(String message) {
		super(message);
	}

	public WrongFormatOfParameterException(String message, Throwable cause) {
		super(message, cause);
	}
}
