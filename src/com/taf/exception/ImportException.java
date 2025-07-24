package com.taf.exception;

public class ImportException extends Exception {

	private static final long serialVersionUID = 6190920797625046289L;

	private static final String ARROW = "->";
	private static final String ERROR_FORMAT = "[%s] %s %s";
	
	private String shortMessage;
	
	public ImportException(Class<?> initiator, String message) {
		super(ERROR_FORMAT.formatted(initiator.getName(), ARROW, message));
		
		this.shortMessage = message;
		while (shortMessage.contains(ARROW)) {
			int index = shortMessage.indexOf(ARROW) + ARROW.length();
			shortMessage = shortMessage.substring(index).strip();
		}
	}
	
	public String getShortMessage() {
		return shortMessage;
	}

}
