package com.taf.event;

public class ProjectRunSpecialErrorEvent implements Event {

	public enum ErrorType {
		MODULE_NOT_FOUND_ERROR;
	}

	private ErrorType errorType;
	
	public ProjectRunSpecialErrorEvent(ErrorType errorType) {
		this.errorType = errorType;
	}

	public ErrorType getErrorType() {
		return errorType;
	}
	
}
