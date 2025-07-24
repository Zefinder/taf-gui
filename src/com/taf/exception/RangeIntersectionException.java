package com.taf.exception;

public class RangeIntersectionException extends Exception {

	private static final long serialVersionUID = 2338098874449607737L;

	private static final String ARROW = "->";
	private static final String ERROR_FORMAT = "[%s] %s %s";
	
	private String shortMessage;
	
	public RangeIntersectionException(Class<?> initiator, String message) {
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
