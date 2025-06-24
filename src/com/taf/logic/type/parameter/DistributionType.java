package com.taf.logic.type.parameter;

public enum DistributionType {
	UNIFORM("u"), NORMAL("n"), INTERVAL("i");
	
	private String distributionString;
	
	private DistributionType(String distributionString) {
		this.distributionString = distributionString;
	}
	
	public String getDistributionString() {
		return distributionString;
	}
	
	public static DistributionType fromDistributionString(String distributionString) {
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
