package com.taf.exception;

public class ParseException extends Exception {

	private static final long serialVersionUID = 4693658875016381055L;

	private static final String ARROW = "->";
	private static final String ERROR_FORMAT = "[%s] %s %s";
	
	private String shortMessage;
	
	public ParseException(Class<?> initiator, String message) {
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
