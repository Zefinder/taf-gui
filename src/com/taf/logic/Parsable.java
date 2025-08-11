package com.taf.logic;

import com.taf.exception.ParseException;

public interface Parsable {

	String valueToString();
	
	void stringToValue(String stringValue) throws ParseException;
	
}
