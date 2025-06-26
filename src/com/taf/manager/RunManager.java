package com.taf.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunManager extends Manager {

	private static final RunManager instance = new RunManager();

	private File runDirectory;

	private static final String SETTINGS_FILE_NAME = "settings.xml";
	private static final String GENERATE_FILE_NAME = "Generate.py";
	private static final String EXPORT_FILE_NAME = "Export.py";

	private static final String SETTINGS_FILE_TEMPLATE = """
			<settings>
			%s
			</settings>
			""";

	private static final String GENERATE_FILE_CONTENT = """
			import Taf

			myTaf = Taf.CLI()
			myTaf.do_parse_template()
			myTaf.do_generate()
			""";

	private static final String EXPORT_FILE_CONTENT = """
			def export(root_node, path):
				raise Exception("TODO write the export file")
			""";

	public static final String TEMPLATE_PATH_STRING = "template_path";
	public static final String TEMPLATE_FILE_NAME_STRING = "template_file_name";
	public static final String EXPERIMENT_PATH_STRING = "experiment_path";
	public static final String EXPERIMENT_FOLDER_NAME_STRING = "experiment_folder_name";
	public static final String NB_TEST_CASES_STRING = "nb_test_cases";
	public static final String TEST_CASE_FOLDER_NAME_STRING = "test_case_folder_name";
	public static final String NB_TEST_ARTIFACTS_STRING = "nb_test_artifacts";
	public static final String TEST_ARTIFACT_FOLDER_NAME_STRING = "test_artifact_folder_name";
	public static final String PARAMETER_MAX_NB_INSTANCES_STRING = "parameter_max_nb_instances";
	public static final String STRING_PARAMETER_MAX_SIZE_STRING = "string_parameter_max_size";
	public static final String NODE_MAX_NB_INSTANCES_STRING = "node_max_nb_instances";
	public static final String MAX_BACKTRACKING_STRING = "max_backtracking";
	public static final String MAX_DIVERSITY_STRING = "max_diversity";
	public static final String Z3_TIMEOUT_STRING = "z3_timeout";

	private static final String PATH_TYPE_STRING = "path";
	private static final String FILE_TYPE_STRING = "file";
	private static final String FOLDER_TYPE_STRING = "folder";
	private static final String INTEGER_TYPE_STRING = "integer";

	private static final String[] TEMPLATE_PATH_PAIR = new String[] { TEMPLATE_PATH_STRING, PATH_TYPE_STRING };
	private static final String[] TEMPLATE_FILE_NAME_PAIR = new String[] { TEMPLATE_FILE_NAME_STRING,
			FILE_TYPE_STRING };
	private static final String[] EXPERIMENT_PATH_PAIR = new String[] { EXPERIMENT_PATH_STRING, PATH_TYPE_STRING };
	private static final String[] EXPERIMENT_FOLDER_NAME_PAIR = new String[] { EXPERIMENT_FOLDER_NAME_STRING,
			FOLDER_TYPE_STRING };
	private static final String[] NB_TEST_CASES_PAIR = new String[] { NB_TEST_CASES_STRING, INTEGER_TYPE_STRING };
	private static final String[] TEST_CASE_FOLDER_NAME_PAIR = new String[] { TEST_CASE_FOLDER_NAME_STRING,
			FOLDER_TYPE_STRING };
	private static final String[] NB_TEST_ARTIFACTS_PAIR = new String[] { NB_TEST_ARTIFACTS_STRING,
			INTEGER_TYPE_STRING };
	private static final String[] TEST_ARTIFACT_FOLDER_NAME_PAIR = new String[] { TEST_ARTIFACT_FOLDER_NAME_STRING,
			FOLDER_TYPE_STRING };
	private static final String[] PARAMETER_MAX_NB_INSTANCES_PAIR = new String[] { PARAMETER_MAX_NB_INSTANCES_STRING,
			INTEGER_TYPE_STRING };
	private static final String[] STRING_PARAMETER_MAX_SIZE_PAIR = new String[] { STRING_PARAMETER_MAX_SIZE_STRING,
			INTEGER_TYPE_STRING };
	private static final String[] NODE_MAX_NB_INSTANCES_PAIR = new String[] { NODE_MAX_NB_INSTANCES_STRING,
			INTEGER_TYPE_STRING };
	private static final String[] MAX_BACKTRACKING_PAIR = new String[] { MAX_BACKTRACKING_STRING, INTEGER_TYPE_STRING };
	private static final String[] MAX_DIVERSITY_PAIR = new String[] { MAX_DIVERSITY_STRING, INTEGER_TYPE_STRING };
	private static final String[] Z3_TIMEOUT_PAIR = new String[] { Z3_TIMEOUT_STRING, INTEGER_TYPE_STRING };

	private static final String SETTING_PATTERN_FORMAT = "<parameter[\s]+name=\"%s\"[\s]+type=\"%s\"[\s]+value=\"([^\"]*)\"[\s]*/[\s]*>";
	private static final String SETTING_LINE_FORMAT = "<parameter name=\"%s\" type=\"%s\" value=\"%s\" />\n";

	private static final String DEFAULT_CHAR_VALUE = "";
	private static final int DEFAULT_INTEGER_VALUE = -1;

	private String templatePath;
	private String templateFileName;
	private String experimentPath;
	private String experimentFolderName;
	private int nbTestCases;
	private String testCaseFolderName;
	private int nbTestArtifacts;
	private String testArtifactFolderName;
	private int parameterMaxNbInstances;
	private int stringParameterMaxSize;
	private int nodeMaxNbInstances;
	private int maxBacktracking;
	private int maxDiversity;
	private int z3Timeout;

	private RunManager() {
		resetValues();
	}

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
		File settingsFile = new File(runDirectory.getAbsolutePath() + File.separator + SETTINGS_FILE_NAME);
		if (settingsFile.exists()) {
			String rawSettings = "";
			try (BufferedReader reader = new BufferedReader(new FileReader(settingsFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					// No need to add new lines
					rawSettings += line;
				}
			} catch (IOException e) {
				ConstantManager
						.showError("An error occured when trying to read the settings file... Default settings set!");
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

	private Optional<String> searchArgumentInSettings(String rawSettings, String[] argumentPair) {
		Pattern pattern = Pattern.compile(SETTING_PATTERN_FORMAT.formatted(argumentPair[0], argumentPair[1]));
		Matcher m = pattern.matcher(rawSettings);
		if (m.find()) {
			return Optional.of(m.group(1));
		}

		return Optional.empty();
	}

	public void run() throws IOException {
		File settingsFile = new File(runDirectory.getAbsolutePath() + File.separator + SETTINGS_FILE_NAME);
		File generateFile = new File(runDirectory.getAbsolutePath() + File.separator + GENERATE_FILE_NAME);
		File exportFile = new File(runDirectory.getAbsolutePath() + File.separator + EXPORT_FILE_NAME);

		// Create Generate.py and Export.py if they do not exist
		if (!generateFile.exists()) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(generateFile))) {
				writer.write(GENERATE_FILE_CONTENT);
			}
		}

		if (!exportFile.exists()) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(exportFile))) {
				writer.write(EXPORT_FILE_CONTENT);
			}
		}

		// Generate settings.xml
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

		// Run TAF
	}

	private String formatParameter(String parameterValue, String[] parameterPair) {
		return SETTING_LINE_FORMAT.formatted(parameterPair[0], parameterPair[1], parameterValue);
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getTemplateFileName() {
		return templateFileName;
	}

	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}

	public String getExperimentPath() {
		return experimentPath;
	}

	public void setExperimentPath(String experimentPath) {
		this.experimentPath = experimentPath;
	}

	public String getExperimentFolderName() {
		return experimentFolderName;
	}

	public void setExperimentFolderName(String experimentFolderName) {
		this.experimentFolderName = experimentFolderName;
	}

	public int getNbTestCases() {
		return nbTestCases;
	}

	public void setNbTestCases(int nbTestCases) {
		this.nbTestCases = nbTestCases;
	}

	public String getTestCaseFolderName() {
		return testCaseFolderName;
	}

	public void setTestCaseFolderName(String testCaseFolderName) {
		this.testCaseFolderName = testCaseFolderName;
	}

	public int getNbTestArtifacts() {
		return nbTestArtifacts;
	}

	public void setNbTestArtifacts(int nbTestArtifacts) {
		this.nbTestArtifacts = nbTestArtifacts;
	}

	public String getTestArtifactFolderName() {
		return testArtifactFolderName;
	}

	public void setTestArtifactFolderName(String testArtifactFolderName) {
		this.testArtifactFolderName = testArtifactFolderName;
	}

	public int getParameterMaxNbInstances() {
		return parameterMaxNbInstances;
	}

	public void setParameterMaxNbInstances(int parameterMaxNbInstances) {
		this.parameterMaxNbInstances = parameterMaxNbInstances;
	}

	public int getStringParameterMaxSize() {
		return stringParameterMaxSize;
	}

	public void setStringParameterMaxSize(int stringParameterMaxSize) {
		this.stringParameterMaxSize = stringParameterMaxSize;
	}

	public int getNodeMaxNbInstances() {
		return nodeMaxNbInstances;
	}

	public void setNodeMaxNbInstances(int nodeMaxNbInstances) {
		this.nodeMaxNbInstances = nodeMaxNbInstances;
	}

	public int getMaxBacktracking() {
		return maxBacktracking;
	}

	public void setMaxBacktracking(int maxBacktracking) {
		this.maxBacktracking = maxBacktracking;
	}

	public int getMaxDiversity() {
		return maxDiversity;
	}

	public void setMaxDiversity(int maxDiversity) {
		this.maxDiversity = maxDiversity;
	}

	public int getZ3Timeout() {
		return z3Timeout;
	}

	public void setZ3Timeout(int z3Timeout) {
		this.z3Timeout = z3Timeout;
	}

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
	}

	@Override
	public void initManager() {
	}

	public static RunManager getInstance() {
		return instance;
	}
}
