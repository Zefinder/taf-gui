package com.taf.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class SettingsManager extends Manager {

	private static final SettingsManager instance = new SettingsManager();

	private static final String SETTINGS_FILE_NAME = "taf_settings.properties";
	private static final String TAF_PATH_PROPERTY = "taf_path";

	private static final String DEFAULT_TAF_PATH = "";

	private static final String SETTINGS_FILE_CREATION_ERROR = "Something happened when trying to create the settings file: ";
	private static final String SETTINGS_FILE_READ_ERROR = "Something happened when trying to read the settings file: ";
	private static final String SETTINGS_FILE_WRITE_ERROR = "Something happened when trying to write the settings file: ";

	private File settingsFile;
	private Properties settings;
	
	private SettingsManager() {
	}

	@Override
	public void initManager() {
		// Create the settings file if it does not exist
		settingsFile = new File(SaveManager.getInstance().getMainDirectoryPath() + File.separator + SETTINGS_FILE_NAME);
		if (!settingsFile.exists()) {
			try {
				settingsFile.createNewFile();
			} catch (IOException e) {
				ConstantManager.showError(SETTINGS_FILE_CREATION_ERROR + e.getMessage());
				e.printStackTrace();
			}
		}

		// Read the settings file
		settings = new Properties();
		try (BufferedReader reader = new BufferedReader(new FileReader(settingsFile))) {
			settings.load(reader);
		} catch (IOException e) {
			ConstantManager.showError(SETTINGS_FILE_READ_ERROR + e.getMessage());
			e.printStackTrace();
		}
		
		// Taf path property
		if (!settings.containsKey(TAF_PATH_PROPERTY)) {
			settings.setProperty(TAF_PATH_PROPERTY, DEFAULT_TAF_PATH);
		}
	}

	public String getTafDirectory() {
		return settings.getProperty(TAF_PATH_PROPERTY);
	}

	public void setTafDirectory(String tafDirectory) {
		settings.setProperty(TAF_PATH_PROPERTY, tafDirectory);
		writeSettings();
	}
	
	private void writeSettings() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile))) {
			settings.store(writer, "");
		} catch (IOException e) {
			ConstantManager.showError(SETTINGS_FILE_WRITE_ERROR + e.getMessage());
			e.printStackTrace();
		}
	}

	public static SettingsManager getInstance() {
		return instance;
	}

}
