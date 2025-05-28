package com.taf.event;

public class FieldNameChangedEvent implements Event {

	private String oldName;
	private String newName;
	
	public FieldNameChangedEvent(String oldName, String newName) {
		this.oldName = oldName;
		this.newName = newName;
	}
	
	public String getOldName() {
		return oldName;
	}
	
	public String getNewName() {
		return newName;
	}

}
