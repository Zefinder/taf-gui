package com.taf.logic.type.parameter;

import com.taf.exception.ParseException;

public enum DistributionType {
	UNIFORM("u"), NORMAL("n"), INTERVAL("i");
	
	private static final String NULL_ERROR_MESSAGE = "Distribution type must not be null!";
	
	private String distributionString;
	
	private DistributionType(String distributionString) {
		this.distributionString = distributionString;
	}
	
	public String getDistributionString() {
		return distributionString;
	}
	
	public static DistributionType fromDistributionString(String distributionString) throws ParseException {
		if (distributionString == null) {
			throw new ParseException(DistributionType.class, NULL_ERROR_MESSAGE);
		}
		
		return switch (distributionString) {
		case "u": 			
			yield UNIFORM;
		
		case "n":
			yield NORMAL;
			
		case "i":
			yield INTERVAL;
		
		default:
			yield UNIFORM;
		};
	}
	
}
