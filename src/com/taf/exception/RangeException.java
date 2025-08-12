package com.taf.exception;

public class RangeException extends Exception {

	private static final long serialVersionUID = -436057787451158031L;
	private static final String ARROW = "->";
	private static final String ERROR_FORMAT = "[%s] %s %s";
	
	private String shortMessage;
	
	public RangeException(Class<?> initiator, String message) {
		super(ERROR_FORMAT.formatted(initiator.getName(), ARROW, message));
		
		this.shortMessage = message;
		while (shortMessage.contains(ARROW)) {
			shortMessage = shortMessage.split(ARROW)[1].strip();
		}
	}
	
	public String getShortMessage() {
		return shortMessage;
	}

}
