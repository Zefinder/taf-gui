/*
 * Copyright or © or Copr.
 * 
 * This software is a computer program whose purpose is to generate random test
 * case from a template file describing the data model.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-B license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package com.taf.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import com.taf.annotation.ManagerImpl;
import com.taf.annotation.Priority;
import com.taf.util.Consts;

/**
 * The SettingsManager manager focuses on loading and saving program settings.
 * 
 * @see Manager
 *
 * @author Adrien Jakubiak
 */
@ManagerImpl(priority = Priority.LOW)
public class SettingsManager implements Manager {

	private static final String SETTINGS_FILE_CREATION_ERROR = "Something happened when trying to create the settings file: ";

	private static final String SETTINGS_FILE_READ_ERROR = "Something happened when trying to read the settings file: ";

	private static final String SETTINGS_FILE_WRITE_ERROR = "Something happened when trying to write the settings file: ";
	
	/** The manager instance. */
	private static final SettingsManager instance = new SettingsManager();

	/** The settings file name. */
	private static final String SETTINGS_FILE_NAME = "taf_settings.properties";

	/** The taf path property. */
	private static final String TAF_PATH_PROPERTY = "taf_path";

	/** The default taf path value. */
	private static final String DEFAULT_TAF_PATH = "";

	/**
	 * Gets the single instance of SettingsManager.
	 *
	 * @return single instance of SettingsManager
	 */
	public static final SettingsManager getInstance() {
		return instance;
	}

	/** The settings file. */
	private File settingsFile;

	/** The settings properties. */
	private Properties settings;

	private SettingsManager() {
	}

	@Override
	public void clear() {
		// Nothing to do here
	}

	/**
	 * Returns the taf directory.
	 *
	 * @return the taf directory
	 */
	public String getTafDirectory() {
		return settings.getProperty(TAF_PATH_PROPERTY);
	}

	@Override
	public void init() {
		// Create the settings file if it does not exist
		settingsFile = new File(SaveManager.getInstance().getMainDirectoryPath() + File.separator + SETTINGS_FILE_NAME);
		if (!settingsFile.exists()) {
			try {
				settingsFile.createNewFile();
			} catch (IOException e) {
				Consts.showError(SETTINGS_FILE_CREATION_ERROR + e.getMessage());
				e.printStackTrace();
			}
		}

		// Read the settings file
		settings = new Properties();
		try (BufferedReader reader = new BufferedReader(new FileReader(settingsFile))) {
			settings.load(reader);
		} catch (IOException e) {
			Consts.showError(SETTINGS_FILE_READ_ERROR + e.getMessage());
			e.printStackTrace();
		}

		// Taf path property
		if (!settings.containsKey(TAF_PATH_PROPERTY)) {
			settings.setProperty(TAF_PATH_PROPERTY, DEFAULT_TAF_PATH);
		}
	}

	/**
	 * Sets the taf directory.
	 *
	 * @param tafDirectory the new taf directory
	 */
	public void setTafDirectory(String tafDirectory) {
		settings.setProperty(TAF_PATH_PROPERTY, tafDirectory);
		writeSettings();
	}

	/**
	 * Write settings to the properties file.
	 */
	private void writeSettings() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile))) {
			settings.store(writer, "");
		} catch (IOException e) {
			Consts.showError(SETTINGS_FILE_WRITE_ERROR + e.getMessage());
			e.printStackTrace();
		}
	}

}
