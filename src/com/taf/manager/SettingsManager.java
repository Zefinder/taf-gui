package com.taf.manager;

public class SettingsManager extends Manager {

	private static final SettingsManager instance = new SettingsManager();
	
	private String tafDirectory;
	
	private SettingsManager() {
	}

	@Override
	public void initManager() {
		tafDirectory = "";
	}
	
	public String getTafDirectory() {
		return tafDirectory;
	}
	
	public void setTafDirectory(String tafDirectory) {
		this.tafDirectory = tafDirectory;
	}
	
	public static SettingsManager getInstance() {
		return instance;
	}

}
