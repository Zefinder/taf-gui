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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.swing.JTextPane;

import com.taf.annotation.EventMethod;
import com.taf.annotation.ManagerImpl;
import com.taf.annotation.Priority;
import com.taf.event.Event;
import com.taf.event.EventListener;
import com.taf.event.ProcessReadyEvent;
import com.taf.event.ProjectRunAbortedEvent;
import com.taf.event.ProjectRunClosedEvent;
import com.taf.event.ProjectRunSpecialErrorEvent;
import com.taf.event.ProjectRunStartedEvent;
import com.taf.event.ProjectRunStoppedEvent;
import com.taf.util.Consts;
import com.taf.util.ProcessStreamReader;

/**
 * The RunManager manager focuses on running TAF to generate the test cases. It
 * acts as an intermediate between the Python process and the GUI.
 * 
 * @see Manager
 *
 * @author Adrien Jakubiak
 */
@ManagerImpl(priority = Priority.LOW)
public class RunManager implements Manager, EventListener {

	/** The manager instance. */
	private static final RunManager instance = new RunManager();

	/** The settings file name. */
	private static final String SETTINGS_FILE_NAME = "settings.xml";

	/** The generate file name. */
	private static final String GENERATE_FILE_NAME = "Generate.py";

	/** The export file name. */
	private static final String EXPORT_FILE_NAME = "Export.py";

	/** The settings file template. */
	private static final String SETTINGS_FILE_TEMPLATE = """
			<settings>
			%s
			</settings>
			""";

	/** The generate file format. */
	private static final String GENERATE_FILE_FORMAT = """
			import sys
			sys.path.append('%s')

			import Taf

			myTaf = Taf.CLI()
			myTaf.do_parse_template()
			myTaf.do_generate()
			""";

	/** The default export file content. */
	private static final String EXPORT_FILE_CONTENT = """
			def export(root_node, path):
				raise Exception("TODO write the export file")
			""";

	/** The template path string property name. */
	public static final String TEMPLATE_PATH_STRING = "template_path";

	/** The template file name string property name. */
	public static final String TEMPLATE_FILE_NAME_STRING = "template_file_name";

	/** The experiment path string property name. */
	public static final String EXPERIMENT_PATH_STRING = "experiment_path";

	/** The experiment folder name string property name. */
	public static final String EXPERIMENT_FOLDER_NAME_STRING = "experiment_folder_name";

	/** The number of test cases string property name. */
	public static final String NB_TEST_CASES_STRING = "nb_test_cases";

	/** The test case folder name string property name. */
	public static final String TEST_CASE_FOLDER_NAME_STRING = "test_case_folder_name";

	/** The number test artifacts string property name. */
	public static final String NB_TEST_ARTIFACTS_STRING = "nb_test_artifacts";

	/** The test artifact folder name string property name. */
	public static final String TEST_ARTIFACT_FOLDER_NAME_STRING = "test_artifact_folder_name";

	/** The parameter max number instances string property name. */
	public static final String PARAMETER_MAX_NB_INSTANCES_STRING = "parameter_max_nb_instances";

	/** The string parameter maximum size string property name. */
	public static final String STRING_PARAMETER_MAX_SIZE_STRING = "string_parameter_max_size";

	/** The node maximum number instances string property name. */
	public static final String NODE_MAX_NB_INSTANCES_STRING = "node_max_nb_instances";

	/** The maximum backtracking string property name. */
	public static final String MAX_BACKTRACKING_STRING = "max_backtracking";

	/** The maximum diversity string property name. */
	public static final String MAX_DIVERSITY_STRING = "max_diversity";

	/** The z3 timeout string property name. */
	public static final String Z3_TIMEOUT_STRING = "z3_timeout";

	/** The path type. */
	private static final String PATH_TYPE_STRING = "path";

	/** The file type. */
	private static final String FILE_TYPE_STRING = "file";

	/** The folder type. */
	private static final String FOLDER_TYPE_STRING = "folder";

	/** The integer type. */
	private static final String INTEGER_TYPE_STRING = "integer";

	/** The template path type pair. */
	private static final String[] TEMPLATE_PATH_PAIR = new String[] { TEMPLATE_PATH_STRING, PATH_TYPE_STRING };

	/** The template file name type pair. */
	private static final String[] TEMPLATE_FILE_NAME_PAIR = new String[] { TEMPLATE_FILE_NAME_STRING,
			FILE_TYPE_STRING };

	/** The experiment path type pair. */
	private static final String[] EXPERIMENT_PATH_PAIR = new String[] { EXPERIMENT_PATH_STRING, PATH_TYPE_STRING };

	/** The experiment folder name type pair. */
	private static final String[] EXPERIMENT_FOLDER_NAME_PAIR = new String[] { EXPERIMENT_FOLDER_NAME_STRING,
			FOLDER_TYPE_STRING };

	/** The number test cases type pair. */
	private static final String[] NB_TEST_CASES_PAIR = new String[] { NB_TEST_CASES_STRING, INTEGER_TYPE_STRING };

	/** The test case folder name type pair. */
	private static final String[] TEST_CASE_FOLDER_NAME_PAIR = new String[] { TEST_CASE_FOLDER_NAME_STRING,
			FOLDER_TYPE_STRING };

	/** The number test artifacts type pair. */
	private static final String[] NB_TEST_ARTIFACTS_PAIR = new String[] { NB_TEST_ARTIFACTS_STRING,
			INTEGER_TYPE_STRING };

	/** The test artifact folder name type pair. */
	private static final String[] TEST_ARTIFACT_FOLDER_NAME_PAIR = new String[] { TEST_ARTIFACT_FOLDER_NAME_STRING,
			FOLDER_TYPE_STRING };

	/** The parameter maximum number instances type pair. */
	private static final String[] PARAMETER_MAX_NB_INSTANCES_PAIR = new String[] { PARAMETER_MAX_NB_INSTANCES_STRING,
			INTEGER_TYPE_STRING };

	/** The string parameter maximum size type pair. */
	private static final String[] STRING_PARAMETER_MAX_SIZE_PAIR = new String[] { STRING_PARAMETER_MAX_SIZE_STRING,
			INTEGER_TYPE_STRING };

	/** The node maximum number of instances type pair. */
	private static final String[] NODE_MAX_NB_INSTANCES_PAIR = new String[] { NODE_MAX_NB_INSTANCES_STRING,
			INTEGER_TYPE_STRING };

	/** The max backtracking type pair. */
	private static final String[] MAX_BACKTRACKING_PAIR = new String[] { MAX_BACKTRACKING_STRING, INTEGER_TYPE_STRING };

	/** The max diversity type pair. */
	private static final String[] MAX_DIVERSITY_PAIR = new String[] { MAX_DIVERSITY_STRING, INTEGER_TYPE_STRING };

	/** The z3 timeout type pair. */
	private static final String[] Z3_TIMEOUT_PAIR = new String[] { Z3_TIMEOUT_STRING, INTEGER_TYPE_STRING };

	/** The setting pattern format. */
	private static final String SETTING_PATTERN_FORMAT = "<parameter[\s]+name=\"%s\"[\s]+type=\"%s\"[\s]+value=\"([^\"]*)\"[\s]*/[\s]*>";

	/** The setting line format. */
	private static final String SETTING_LINE_FORMAT = "<parameter name=\"%s\" type=\"%s\" value=\"%s\" />\n";

	/** The default char value. */
	private static final String DEFAULT_CHAR_VALUE = "";

	/** The default integer value. */
	private static final int DEFAULT_INTEGER_VALUE = -1;

	/** The default boolean value. */
	private static final boolean DEFAULT_BOOLEAN_VALUE = false;

	private static final String SAVE_ON_QUIT_ERROR_MESSAGE = "Something wrong happened when trying to save the settings: ";

	/**
	 * Gets the single instance of RunManager.
	 *
	 * @return single instance of RunManager
	 */
	public static RunManager getInstance() {
		return instance;
	}

	/** The run directory. */
	private File runDirectory;

	/** The template path. */
	private String templatePath;

	/** The template file name. */
	private String templateFileName;

	/** The experiment path. */
	private String experimentPath;

	/** The experiment folder name. */
	private String experimentFolderName;

	/** The number of test cases. */
	private int nbTestCases;

	/** The test case folder name. */
	private String testCaseFolderName;

	/** The number of test artifacts. */
	private int nbTestArtifacts;

	/** The test artifact folder name. */
	private String testArtifactFolderName;

	/** The parameter maximum number of instances. */
	private int parameterMaxNbInstances;

	/** The string parameter maximum size. */
	private int stringParameterMaxSize;

	/** The node maximum number of instances. */
	private int nodeMaxNbInstances;

	/** The maximum backtracking. */
	private int maxBacktracking;

	/** The maximum diversity. */
	private int maxDiversity;

	/** The z3 timeout. */
	private int z3Timeout;

	/** The delete experiment folder state. */
	private boolean deleteExperimentFolder;

	/** The settings file. */
	private File settingsFile;

	/** The process stream reader. */
	private ProcessStreamReader processStreamReader;

	/** The TAF Python process. */
	private Process process;

	/**
	 * Instantiates a new run manager.
	 */
	private RunManager() {
		processStreamReader = new ProcessStreamReader();
		resetValues();
	}

	@Override
	public void clear() {
		EventManager.getInstance().unregisterEventListener(instance);
	}

	/**
	 * Returns the experiment folder name.
	 *
	 * @return the experiment folder name
	 */
	public String getExperimentFolderName() {
		return experimentFolderName;
	}

	/**
	 * Returns the experiment path.
	 *
	 * @return the experiment path
	 */
	public String getExperimentPath() {
		return experimentPath;
	}

	/**
	 * Returns the maximum backtracking.
	 *
	 * @return the maximum backtracking
	 */
	public int getMaxBacktracking() {
		return maxBacktracking;
	}

	/**
	 * Returns the maximum diversity.
	 *
	 * @return the maximum diversity
	 */
	public int getMaxDiversity() {
		return maxDiversity;
	}

	/**
	 * Returns the number of test artifacts.
	 *
	 * @return the number of test artifacts
	 */
	public int getNbTestArtifacts() {
		return nbTestArtifacts;
	}

	/**
	 * Returns the number of test cases.
	 *
	 * @return the number of test cases
	 */
	public int getNbTestCases() {
		return nbTestCases;
	}

	/**
	 * Returns the node maximum number of instances.
	 *
	 * @return the node maximum number of instances
	 */
	public int getNodeMaxNbInstances() {
		return nodeMaxNbInstances;
	}

	/**
	 * Returns the parameter max number of instances.
	 *
	 * @return the parameter max number of instances
	 */
	public int getParameterMaxNbInstances() {
		return parameterMaxNbInstances;
	}

	/**
	 * Returns the string parameter maximum size.
	 *
	 * @return the string parameter maximum size
	 */
	public int getStringParameterMaxSize() {
		return stringParameterMaxSize;
	}

	/**
	 * Returns the template file name.
	 *
	 * @return the template file name
	 */
	public String getTemplateFileName() {
		return templateFileName;
	}

	/**
	 * Returns the template path.
	 *
	 * @return the template path
	 */
	public String getTemplatePath() {
		return templatePath;
	}

	/**
	 * Returns the test artifact folder name.
	 *
	 * @return the test artifact folder name
	 */
	public String getTestArtifactFolderName() {
		return testArtifactFolderName;
	}

	/**
	 * Returns the test case folder name.
	 *
	 * @return the test case folder name
	 */
	public String getTestCaseFolderName() {
		return testCaseFolderName;
	}

	/**
	 * Returns the z3 timeout.
	 *
	 * @return the z3 timeout
	 */
	public int getZ3Timeout() {
		return z3Timeout;
	}

	@Override
	public void init() {
		EventManager.getInstance().registerEventListener(instance);
	}

	/**
	 * Handling for the {@link ProjectRunClosedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onProjectRunClosed(ProjectRunClosedEvent event) {
		try {
			saveSettings();
		} catch (IOException e) {
			Consts.showError(SAVE_ON_QUIT_ERROR_MESSAGE + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Handler for {@link ProjectRunAbortedEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onRunAborted(ProjectRunAbortedEvent event) {
		if (process != null) {
			process.destroyForcibly();
		}
	}

	/**
	 * Handler for {@link ProjectRunSpecialErrorEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onSpecialRunError(ProjectRunSpecialErrorEvent event) {
		switch (event.getErrorType()) {
		case MODULE_NOT_FOUND_ERROR:
			Consts.showError("Taf module is not found...\n"
					+ "You either specified a bad location or the file was not generated by the GUI.\n"
					+ "Verify that the path points to TAF's src directory and delete the Generate.py file to regenerate it.");
			break;

		default:
			break;
		}
	}

	/**
	 * Prepare run manager for a new run.
	 *
	 * @throws IOException Signals that an I/O exception has occurred when reading
	 *                     the settings file.
	 */
	public void prepareRunManager() throws IOException {
		SaveManager saveManager = SaveManager.getInstance();

		// Reset to default values
		resetValues();

		// Get the run directory so the XML can be exported there
		runDirectory = saveManager.getProjectRunFolder();

		// Get the file name and export in the run directory
		String xmlFileName = saveManager.getRunXMLFileName();
		saveManager.exportToXML(new File(runDirectory.getAbsolutePath() + File.separator + xmlFileName));

		// Set the template file name (can change afterwards)
		templateFileName = xmlFileName;

		// Read the settings file and fill info
		settingsFile = new File(runDirectory.getAbsolutePath() + File.separator + SETTINGS_FILE_NAME);
		if (settingsFile.exists()) {
			String rawSettings = "";
			try (BufferedReader reader = new BufferedReader(new FileReader(settingsFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					// No need to add new lines
					rawSettings += line;
				}
			} catch (IOException e) {
				Consts.showError("An error occured when trying to read the settings file... Default settings set!");
				e.printStackTrace();
			}

			// Search for all arguments in the raw settings
			Optional<String> templatePathOp = searchArgumentInSettings(rawSettings, TEMPLATE_PATH_PAIR);
			if (templatePathOp.isPresent()) {
				templatePath = templatePathOp.get();
			}

			Optional<String> templateFileNameOp = searchArgumentInSettings(rawSettings, TEMPLATE_FILE_NAME_PAIR);
			if (templateFileNameOp.isPresent()) {
				templateFileName = templateFileNameOp.get();
			}

			Optional<String> experimentPathOp = searchArgumentInSettings(rawSettings, EXPERIMENT_PATH_PAIR);
			if (experimentPathOp.isPresent()) {
				experimentPath = experimentPathOp.get();
			}

			Optional<String> experimentFolderNameOp = searchArgumentInSettings(rawSettings,
					EXPERIMENT_FOLDER_NAME_PAIR);
			if (experimentFolderNameOp.isPresent()) {
				experimentFolderName = experimentFolderNameOp.get();
			}

			Optional<String> nbTestCasesOp = searchArgumentInSettings(rawSettings, NB_TEST_CASES_PAIR);
			if (nbTestCasesOp.isPresent()) {
				nbTestCases = Integer.valueOf(nbTestCasesOp.get());
			}

			Optional<String> testCaseFolderNameOp = searchArgumentInSettings(rawSettings, TEST_CASE_FOLDER_NAME_PAIR);
			if (testCaseFolderNameOp.isPresent()) {
				testCaseFolderName = testCaseFolderNameOp.get();
			}

			Optional<String> nbTestArtifactsOp = searchArgumentInSettings(rawSettings, NB_TEST_ARTIFACTS_PAIR);
			if (nbTestArtifactsOp.isPresent()) {
				nbTestArtifacts = Integer.valueOf(nbTestArtifactsOp.get());
			}

			Optional<String> testArtifactFolderNameOp = searchArgumentInSettings(rawSettings,
					TEST_ARTIFACT_FOLDER_NAME_PAIR);
			if (testArtifactFolderNameOp.isPresent()) {
				testArtifactFolderName = testArtifactFolderNameOp.get();
			}

			Optional<String> parameterMaxNbInstancesOp = searchArgumentInSettings(rawSettings,
					PARAMETER_MAX_NB_INSTANCES_PAIR);
			if (parameterMaxNbInstancesOp.isPresent()) {
				parameterMaxNbInstances = Integer.valueOf(parameterMaxNbInstancesOp.get());
			}

			Optional<String> stringParameterMaxSizeOp = searchArgumentInSettings(rawSettings,
					STRING_PARAMETER_MAX_SIZE_PAIR);
			if (stringParameterMaxSizeOp.isPresent()) {
				stringParameterMaxSize = Integer.valueOf(stringParameterMaxSizeOp.get());
			}

			Optional<String> nodeMaxNbInstancesOp = searchArgumentInSettings(rawSettings, NODE_MAX_NB_INSTANCES_PAIR);
			if (nodeMaxNbInstancesOp.isPresent()) {
				nodeMaxNbInstances = Integer.valueOf(nodeMaxNbInstancesOp.get());
			}

			Optional<String> maxBacktrackingOp = searchArgumentInSettings(rawSettings, MAX_BACKTRACKING_PAIR);
			if (maxBacktrackingOp.isPresent()) {
				maxBacktracking = Integer.valueOf(maxBacktrackingOp.get());
			}

			Optional<String> maxDiversityOp = searchArgumentInSettings(rawSettings, MAX_DIVERSITY_PAIR);
			if (maxDiversityOp.isPresent()) {
				maxDiversity = Integer.valueOf(maxDiversityOp.get());
			}

			Optional<String> z3TimeoutOp = searchArgumentInSettings(rawSettings, Z3_TIMEOUT_PAIR);
			if (z3TimeoutOp.isPresent()) {
				z3Timeout = Integer.valueOf(z3TimeoutOp.get());
			}
		}
	}

	/**
	 * Runs the TAF program with the current run settings. This will generate and
	 * export the XML file to the selected location before running. If the
	 * {@link #deleteExperimentFolder} is set to true, it will delete all the
	 * previously generated test artifacts.
	 *
	 * @throws IOException Signals that an I/O exception has occurred when running
	 *                     the process.
	 */
	public void run() throws IOException {
		File generateFile = new File(runDirectory.getAbsolutePath() + File.separator + GENERATE_FILE_NAME);
		File exportFile = new File(runDirectory.getAbsolutePath() + File.separator + EXPORT_FILE_NAME);

		// Create Generate.py and Export.py if they do not exist
		if (!generateFile.exists()) {
			String tafDirectory = SettingsManager.getInstance().getTafDirectory();
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(generateFile))) {
				writer.write(GENERATE_FILE_FORMAT.formatted(tafDirectory));
			}
		}

		if (!exportFile.exists()) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(exportFile))) {
				writer.write(EXPORT_FILE_CONTENT);
			}
		}

		// Generate settings.xml
		saveSettings();

		// Check if must delete the experiment folder
		if (deleteExperimentFolder) {
			File experimentFile = new File(
					runDirectory + File.separator + experimentPath + File.separator + experimentFolderName);

			if (experimentFile.exists()) {
				Path pathToBeDeleted = experimentFile.toPath();
				try (Stream<Path> paths = Files.walk(pathToBeDeleted)) {
					paths.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
				}
			}
		}

		// Send an event to tell the console panel to redirect
		Event event = new ProcessReadyEvent();
		EventManager.getInstance().fireEvent(event);

		// Create and start the TAF process
		ProcessBuilder pb = new ProcessBuilder("python3", generateFile.getAbsolutePath());
		pb.directory(runDirectory);
		process = pb.start();
		event = new ProjectRunStartedEvent();
		EventManager.getInstance().fireEvent(event);

		// Setup the stream reader
		processStreamReader.start(process);

		// Wait for the process to terminate in a new Thread
		new Thread(() -> {
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Close the stream reader
			processStreamReader.stop();

			// Sent stop event
			Event stopEvent = new ProjectRunStoppedEvent();
			EventManager.getInstance().fireEvent(stopEvent);
		}).start();
	}

	/**
	 * Sets the delete experiment folder.
	 *
	 * @param deleteExperimentFolder the new delete experiment folder
	 */
	public void setDeleteExperimentFolder(boolean deleteExperimentFolder) {
		this.deleteExperimentFolder = deleteExperimentFolder;
	}

	/**
	 * Sets the experiment folder name.
	 *
	 * @param experimentFolderName the new experiment folder name
	 */
	public void setExperimentFolderName(String experimentFolderName) {
		this.experimentFolderName = experimentFolderName;
	}

	/**
	 * Sets the experiment path.
	 *
	 * @param experimentPath the new experiment path
	 */
	public void setExperimentPath(String experimentPath) {
		this.experimentPath = experimentPath;
	}

	/**
	 * Sets the maximum backtracking.
	 *
	 * @param maxBacktracking the new maximum backtracking
	 */
	public void setMaxBacktracking(int maxBacktracking) {
		this.maxBacktracking = maxBacktracking;
	}

	/**
	 * Sets the maximum diversity.
	 *
	 * @param maxDiversity the new maximum diversity
	 */
	public void setMaxDiversity(int maxDiversity) {
		this.maxDiversity = maxDiversity;
	}

	/**
	 * Sets the number of test artifacts.
	 *
	 * @param nbTestArtifacts the new number of test artifacts
	 */
	public void setNbTestArtifacts(int nbTestArtifacts) {
		this.nbTestArtifacts = nbTestArtifacts;
	}

	/**
	 * Sets the number of test cases.
	 *
	 * @param nbTestCases the new number of test cases
	 */
	public void setNbTestCases(int nbTestCases) {
		this.nbTestCases = nbTestCases;
	}

	/**
	 * Sets the node maximum number of instances.
	 *
	 * @param nodeMaxNbInstances the new node maximum number of instances
	 */
	public void setNodeMaxNbInstances(int nodeMaxNbInstances) {
		this.nodeMaxNbInstances = nodeMaxNbInstances;
	}

	/**
	 * Sets the parameter max number of instances.
	 *
	 * @param parameterMaxNbInstances the new parameter max number of instances
	 */
	public void setParameterMaxNbInstances(int parameterMaxNbInstances) {
		this.parameterMaxNbInstances = parameterMaxNbInstances;
	}

	/**
	 * Sets the string parameter maximum size.
	 *
	 * @param stringParameterMaxSize the new string parameter maximum size
	 */
	public void setStringParameterMaxSize(int stringParameterMaxSize) {
		this.stringParameterMaxSize = stringParameterMaxSize;
	}

	/**
	 * Sets the template file name.
	 *
	 * @param templateFileName the new template file name
	 */
	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}

	/**
	 * Sets the template path.
	 *
	 * @param templatePath the new template path
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	/**
	 * Sets the test artifact folder name.
	 *
	 * @param testArtifactFolderName the new test artifact folder name
	 */
	public void setTestArtifactFolderName(String testArtifactFolderName) {
		this.testArtifactFolderName = testArtifactFolderName;
	}

	/**
	 * Sets the test case folder name.
	 *
	 * @param testCaseFolderName the new test case folder name
	 */
	public void setTestCaseFolderName(String testCaseFolderName) {
		this.testCaseFolderName = testCaseFolderName;
	}

	/**
	 * Sets the text pane.
	 *
	 * @param textPane the new text pane
	 */
	public void setTextPane(JTextPane textPane) {
		processStreamReader.setTextPane(textPane);
	}

	/**
	 * Sets the z3 timeout.
	 *
	 * @param z3Timeout the new z3 timeout
	 */
	public void setZ3Timeout(int z3Timeout) {
		this.z3Timeout = z3Timeout;
	}

	@Override
	public void unregisterComponents() {
		// Nothing here
	}

	/**
	 * Format a run settings parameter.
	 *
	 * @param parameterValue the parameter value
	 * @param parameterPair  the parameter pair
	 * @return the string
	 */
	private String formatParameter(String parameterValue, String[] parameterPair) {
		return SETTING_LINE_FORMAT.formatted(parameterPair[0], parameterPair[1], parameterValue);
	}

	/**
	 * Reset all values to their default type values.
	 */
	private void resetValues() {
		templatePath = DEFAULT_CHAR_VALUE;
		templateFileName = DEFAULT_CHAR_VALUE;
		experimentPath = DEFAULT_CHAR_VALUE;
		experimentFolderName = DEFAULT_CHAR_VALUE;
		nbTestCases = DEFAULT_INTEGER_VALUE;
		testCaseFolderName = DEFAULT_CHAR_VALUE;
		nbTestArtifacts = DEFAULT_INTEGER_VALUE;
		testArtifactFolderName = DEFAULT_CHAR_VALUE;
		parameterMaxNbInstances = DEFAULT_INTEGER_VALUE;
		stringParameterMaxSize = DEFAULT_INTEGER_VALUE;
		nodeMaxNbInstances = DEFAULT_INTEGER_VALUE;
		maxBacktracking = DEFAULT_INTEGER_VALUE;
		maxDiversity = DEFAULT_INTEGER_VALUE;
		z3Timeout = DEFAULT_INTEGER_VALUE;
		deleteExperimentFolder = DEFAULT_BOOLEAN_VALUE;
	}

	/**
	 * Save the settings for the TAF generation.
	 *
	 * @throws IOException if the settings could not be written.
	 */
	private void saveSettings() throws IOException {
		String settingsParameters = "";
		settingsParameters += formatParameter(templatePath, TEMPLATE_PATH_PAIR);
		settingsParameters += formatParameter(templateFileName, TEMPLATE_FILE_NAME_PAIR);
		settingsParameters += formatParameter(experimentPath, EXPERIMENT_PATH_PAIR);
		settingsParameters += formatParameter(experimentFolderName, EXPERIMENT_FOLDER_NAME_PAIR);
		settingsParameters += formatParameter(String.valueOf(nbTestCases), NB_TEST_CASES_PAIR);
		settingsParameters += formatParameter(testCaseFolderName, TEST_CASE_FOLDER_NAME_PAIR);
		settingsParameters += formatParameter(String.valueOf(nbTestArtifacts), NB_TEST_ARTIFACTS_PAIR);
		settingsParameters += formatParameter(testArtifactFolderName, TEST_ARTIFACT_FOLDER_NAME_PAIR);
		settingsParameters += formatParameter(String.valueOf(parameterMaxNbInstances), PARAMETER_MAX_NB_INSTANCES_PAIR);
		settingsParameters += formatParameter(String.valueOf(stringParameterMaxSize), STRING_PARAMETER_MAX_SIZE_PAIR);
		settingsParameters += formatParameter(String.valueOf(nodeMaxNbInstances), NODE_MAX_NB_INSTANCES_PAIR);
		settingsParameters += formatParameter(String.valueOf(maxBacktracking), MAX_BACKTRACKING_PAIR);
		settingsParameters += formatParameter(String.valueOf(maxDiversity), MAX_DIVERSITY_PAIR);
		settingsParameters += formatParameter(String.valueOf(z3Timeout), Z3_TIMEOUT_PAIR);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile))) {
			writer.write(SETTINGS_FILE_TEMPLATE.formatted(settingsParameters));
		}
	}

	/**
	 * Search the argument in the run settings.
	 *
	 * @param rawSettings  the raw settings
	 * @param argumentPair the argument pair
	 * @return the argument in an optional
	 */
	private Optional<String> searchArgumentInSettings(String rawSettings, String[] argumentPair) {
		Pattern pattern = Pattern.compile(SETTING_PATTERN_FORMAT.formatted(argumentPair[0], argumentPair[1]));
		Matcher m = pattern.matcher(rawSettings);
		if (m.find()) {
			return Optional.of(m.group(1));
		}

		return Optional.empty();
	}
}
