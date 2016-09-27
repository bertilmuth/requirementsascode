package org.requirementsascode.exception;

public class MissingUseCaseStepPartException extends RuntimeException{
	private static final long serialVersionUID = 1154053717206525045L;
	
	public MissingUseCaseStepPartException(String message) {
		super(message);
	}
}
