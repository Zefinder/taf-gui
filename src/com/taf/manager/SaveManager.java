package com.taf.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import com.taf.event.ProjectClosedEvent;
import com.taf.exception.ImportException;
import com.taf.exception.ParseException;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.constraint.parameter.ConstraintParameter;
import com.taf.logic.constraint.parameter.ConstraintParameterFactory;
import com.taf.logic.field.Field;
import com.taf.logic.field.Node;
import com.taf.logic.field.Parameter;
import com.taf.logic.field.Root;
import com.taf.logic.field.Type;
import com.taf.logic.type.BooleanType;
import com.taf.logic.type.FieldType;
import com.taf.logic.type.IntegerType;
import com.taf.logic.type.NodeType;
import com.taf.logic.type.RealType;
import com.taf.logic.type.StringType;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.logic.type.parameter.TypeParameterFactory;
import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;
import com.taf.util.OSValidator;
import com.taf.util.XMLTafReader;

/**
 * <p>
 * Manager in charge of saving and reading TAF project files. The file syntax is
 * easy to understand:
 * <ul>
 * <li>Each element has arguments with the following regex:
 * arg_name="arg_value". There must be no space between the name and the equal
 * sign and between the equal sign and the double-quote.
 * <li>The argument separator is a space just to be readable (it is not
 * mandatory to have a separator since we match pattern.
 * <lI>Each node has an implicit id (starting with 0 being the root).
 * <li>Each element must have a non empty <code>entity</code>,
 * <code>name</code>, and <code>parent</code>. The root's parent id is -1.
 * <li>Entity is either <code>parameter</code>, <code>node</code>, or
 * <code>constraint</code>
 * </ul>
 * </p>
 * 
 * <p>
 * The grammar for each line is the following: (value is a non-empty string that
 * does not contain the double-quote character (&quot;)
 * 
 * <pre>
 * tEQ   ::= '='
 * tQUOT ::= '&quot;'
 * 
 * tENTITY ::= 'entity'
 * tPARENT ::= 'parent'
 * tNAME   ::= 'name'
 * 
 * line ::= line_args
 *       |  line_args args
 *       
 * line_args ::= entity_arg parent_arg name_arg
 * 
 * entity_arg ::= tENTITY arg_value
 * parent_arg ::= tPARENT arg_value
 * name_arg   ::= tNAME arg_value
 * 
 * args ::= arg args
 *       |  arg    
 *            
 * arg       ::= arg_name arg_value
 * arg_value ::= tEQ tQUOT value tQUOT
 * </pre>
 * 
 * <code>arg_name</code> is the type-specific parameter of the entity (for
 * instance, strings will have <code>values</code> and <code>weights</code>).
 * </p>
 * 
 * <p>
 * An example of this can be:
 * 
 * <pre>
 * entity="node" parent="-1" name="root"
 * entity="node" parent="0" name="node" nb_instances="2"
 * entity="parameter" parent="1" name="node_int" type="integer"
 * entity="parameter" parent="0" name="test" type="boolean"
 * </pre>
 * 
 * This will create the following tree:
 * 
 * <pre>
 * root
 * |___ node: node
 * |    |___ node_int: integer
 * |
 * |___ test: boolean
 * </pre>
 * </p>
 */
public class SaveManager extends Manager {

	private static final String XML_HEADER = "<?xml version=\"1.0\"?>\n\n\n";

	private static final String separator = ConstantManager.PARAMETER_SEPARATOR;
	private static final String newLine = ConstantManager.LINE_JUMP;
	private static final String format = ConstantManager.PARAMETER_STRING_FORMAT + separator;

	private static final String WINDOWS_USER_BASE_MAIN_DIR = System.getenv("APPDATA");
	private static final String UNIX_MAC_USER_BASE_MAIN_DIR = System.getProperty("user.home");
	private static final String OTHER_USER_BASE_MAIN_DIR = ".";

	private static final String TAF_DIRECTORY_NAME = "tafgui";
	private static final String RUN_DIRECTORY_NAME = "run";

	private static final String SAVE_ARGUMENT_FORMAT = "\\b%s=\"([^\"]+)\""; // Arguments must be separated
	private static final String ENTITY_ARGUMENT = "entity";
	private static final String PARENT_ARGUMENT = "parent";
	private static final String NAME_ARGUMENT = "name";
	private static final String TYPE_ARGUMENT = "type";

	private static final int ROOT_PARENT = -1;

	private static final String ENTITY_MISSING_ERROR_MESSAGE = "The entity must have a type!";
	private static final String PARENT_MISSING_ERROR_MESSAGE = "The entity must have a parent!";
	private static final String PARENT_UNKNWON_FORMAT_ERROR_MESSAGE = "The parent %d does not exist!";
	private static final String NAME_MISSING_ERROR_MESSAGE = "Entity must have a name!";
	private static final String PARAMETER_TYPE_ERROR_MESSAGE = "Parameter must have a type";
	private static final String PARAMETER_UNEXPECTED_ERROR_MESSAGE = "Unexpected parameter type value: ";
	private static final String PARAMETER_TYPE_MISSING_FORMAT_ERROR_MESSAGE = "Type %s must have the \"%s\" argument";
	private static final String NUMBER_FORMAT_EXCEPTION_ERROR_MESSAGE = "An argument value was not a number (no more information)";
	private static final String PARSE_EXCEPTION_FORMAT_ERROR_MESSAGE = "%s (line %d)";
	private static final String IMPORT_EXCEPTION_ERROR_MESSAGE = "An error occured when importing the file: ";

	private static final SaveManager instance = new SaveManager();

	private final Set<File> projectNames;
	private final OSValidator OS;

	private String mainDirectory;
	private File mainDirectoryFile;
	private String runDirectory;
	private File runDirectoryFile;

	private File projectFile;
	private Root projectRoot;

	public SaveManager() {
		projectNames = new HashSet<File>();
		OS = OSValidator.getOS();
	}

	@Override
	public void initManager() {
		// For each OS, check if the main directory is present
		switch (OS) {
		case WINDOWS:
			// Create in appdata
			mainDirectory = WINDOWS_USER_BASE_MAIN_DIR;
			break;

		case MAC:
		case UNIX:
			// Create in home
			mainDirectory = UNIX_MAC_USER_BASE_MAIN_DIR;
			break;

		default:
			// Use the current directory
			mainDirectory = OTHER_USER_BASE_MAIN_DIR;
			break;
		}

		mainDirectory += File.separator + TAF_DIRECTORY_NAME;
		mainDirectoryFile = new File(mainDirectory);
		runDirectory = mainDirectory + File.separator + RUN_DIRECTORY_NAME;
		runDirectoryFile = new File(runDirectory);

		// Create all directories if do not exist
		initCreation();

		// Register all project files
		initProjectFiles();
	}

	private void initCreation() {
		if (!mainDirectoryFile.exists()) {
			mainDirectoryFile.mkdirs();
		}

		if (!runDirectoryFile.exists()) {
			runDirectoryFile.mkdir();
		}
	}

	private void initProjectFiles() {
		File[] tafFiles = mainDirectoryFile.listFiles((dir, name) -> name.endsWith(ConstantManager.TAF_FILE_EXTENSION));
		for (File tafFile : tafFiles) {
			projectNames.add(tafFile);
		}
	}

	private Optional<String> getArgument(String line, String argumentName) {
		Pattern argumentPattern = Pattern.compile(SAVE_ARGUMENT_FORMAT.formatted(argumentName));
		Matcher m = argumentPattern.matcher(line);
		if (m.find()) {
			return Optional.of(m.group(1));
		}

		return Optional.empty();
	}

	private File getProjectFileFromName(String projectName) {
		return new File(mainDirectory + File.separator + projectName);
	}

	/**
	 * Open the project with the specified file. The save file syntax is defined in
	 * {@link SaveManager}. It returns the root of the project, containing all nodes
	 * and parameters.
	 * 
	 * @return the project root.
	 * @throws ParseException
	 * @throws FileNotFoundException
	 */
	public Root openProject(String projectName) throws IOException, ParseException {
		this.projectFile = getProjectFileFromName(projectName);
		Root root = null;
		int lineNumber = 1;
		List<Type> typeList = new ArrayList<Type>();

		try (BufferedReader reader = new BufferedReader(new FileReader(projectFile))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// If line is empty, skip
				if (line.isBlank()) {
					lineNumber++;
					continue;
				}

				// Get entity type
				Optional<String> entityTypeOp = getArgument(line, ENTITY_ARGUMENT);
				if (entityTypeOp.isEmpty()) {
					throw new ParseException(this.getClass(), ENTITY_MISSING_ERROR_MESSAGE);
				}
				String entityType = entityTypeOp.get();

				// Get parent id
				Optional<String> parentIdOp = getArgument(line, PARENT_ARGUMENT);
				if (parentIdOp.isEmpty()) {
					throw new ParseException(this.getClass(), PARENT_MISSING_ERROR_MESSAGE);
				}
				int parentId = Integer.valueOf(parentIdOp.get());

				// Check if parent exists
				if (parentId < -1 || parentId >= typeList.size()) {
					throw new ParseException(this.getClass(), PARENT_UNKNWON_FORMAT_ERROR_MESSAGE.formatted(parentId));
				}

				// TODO Check if multiple roots

				// Search for the name
				Optional<String> entityNameOp = getArgument(line, NAME_ARGUMENT);
				if (entityNameOp.isEmpty()) {
					throw new ParseException(this.getClass(), NAME_MISSING_ERROR_MESSAGE);
				}
				String entityName = entityNameOp.get();

				switch (entityType) {
				case ConstantManager.TYPE_ENTITY_NAME:
					Type type = new Type(entityName);

					// Parent id must be 0
					// TODO send a warning

					typeList.get(0).addEntity(type);
					typeList.add(type);

					// Add to type manager
					TypeManager.getInstance().addCustomNodeType(type);
					break;

				case ConstantManager.NODE_ENTITY_NAME:
					Node node;

					if (parentId == -1) {
						// This is the root node
						root = new Root(entityName);
						node = root;

					} else {
						// Create a node type and fill parameters
						NodeType nodeType = new NodeType();
						addParametersToFieldType(nodeType, line, MinMaxTypeParameterType.INSTANCE);

						// Create node and add it to its parent
						node = new Node(entityName, nodeType);
						typeList.get(parentId).addEntity(node);

						// Add to type manager as a possible reference
						TypeManager.getInstance().addCustomReference(node);
						
						// Check if node has a type. If so, tell it to the type manager
						if (node.hasType()) {
							TypeManager.getInstance().setNodeType(node.getTypeName(), node);
						} else if (node.hasRef()) {
							TypeManager.getInstance().setNodeRef(node.getTypeName(), node);
						}
					}

					typeList.add(node);
					break;

				case ConstantManager.PARAMETER_ENTITY_NAME:
					// Get parameter type
					Optional<String> typeNameOp = getArgument(line, TYPE_ARGUMENT);
					if (typeNameOp.isEmpty()) {
						throw new ParseException(this.getClass(), PARAMETER_TYPE_ERROR_MESSAGE);
					}
					String typeName = typeNameOp.get();
					TypeParameterFactory.MinMaxTypeParameterType minMaxType;
					FieldType fieldType = switch (typeName) {
					case BooleanType.TYPE_NAME: {
						minMaxType = MinMaxTypeParameterType.NONE;
						yield new BooleanType();
					}

					case IntegerType.TYPE_NAME: {
						minMaxType = MinMaxTypeParameterType.INTEGER;
						yield new IntegerType();
					}

					case RealType.TYPE_NAME: {
						minMaxType = MinMaxTypeParameterType.REAL;
						yield new RealType();
					}

					case StringType.TYPE_NAME: {
						minMaxType = MinMaxTypeParameterType.NONE;
						yield new StringType();
					}

					default:
						throw new ParseException(this.getClass(), PARAMETER_UNEXPECTED_ERROR_MESSAGE + typeName);
					};

					addParametersToFieldType(fieldType, line, minMaxType);

					Parameter parameter = new Parameter(entityName, fieldType);
					typeList.get(parentId).addEntity(parameter);
					break;

				case ConstantManager.CONSTRAINT_ENTITY_NAME:
					Constraint constraint = new Constraint(entityName);
					for (String constraintParameterName : ConstraintParameter.getConstraintParameterNames()) {
						Optional<String> optionalConstraintParameterOp = getArgument(line, constraintParameterName);
						if (!optionalConstraintParameterOp.isEmpty()) {
							String constraintParameterValue = optionalConstraintParameterOp.get();
							ConstraintParameter constraintParameter = ConstraintParameterFactory
									.createConstraintParameter(constraintParameterName, constraintParameterValue);
							constraint.addConstraintParameter(constraintParameter);
						}
					}

					typeList.get(parentId).addEntity(constraint);
					break;
				}

				lineNumber++;
			}
		} catch (NumberFormatException e) {
			throwParseException(NUMBER_FORMAT_EXCEPTION_ERROR_MESSAGE, lineNumber);
		} catch (ParseException e1) {
			throwParseException(e1.getMessage(), lineNumber);
		}

		this.projectRoot = root;
		return root;
	}

	private void addParametersToFieldType(FieldType fieldType, String line, MinMaxTypeParameterType minMaxType)
			throws ParseException {
		// Add mandatory parameters
		for (String mandatoryTypeName : fieldType.getMandatoryParametersName()) {
			Optional<String> mandatoryTypeOp = getArgument(line, mandatoryTypeName);
			if (mandatoryTypeOp.isEmpty()) {
				throw new ParseException(this.getClass(),
						PARAMETER_TYPE_MISSING_FORMAT_ERROR_MESSAGE.formatted(fieldType.getName(), mandatoryTypeName));
			}
			String typeParameterValue = mandatoryTypeOp.get();

			TypeParameter typeParameter = TypeParameterFactory.createTypeParameter(mandatoryTypeName,
					typeParameterValue, minMaxType);
			fieldType.addTypeParameter(typeParameter);
		}

		// Add optional parameters
		for (String optionalTypeName : fieldType.getOptionalParametersName()) {
			Optional<String> optionalTypeOp = getArgument(line, optionalTypeName);
			if (!optionalTypeOp.isEmpty()) {
				String typeParameterValue = optionalTypeOp.get();

				TypeParameter typeParameter = TypeParameterFactory.createTypeParameter(optionalTypeName,
						typeParameterValue, minMaxType);
				fieldType.addTypeParameter(typeParameter);
			}
		}
	}

	private void writeEntityArguments(BufferedWriter writer, String entityString, int nodeId, String name)
			throws IOException {
		writer.write(format.formatted(ENTITY_ARGUMENT, entityString));
		writer.write(format.formatted(PARENT_ARGUMENT, nodeId));
		writer.write(format.formatted(NAME_ARGUMENT, name));
	}

	private int writeRoot(BufferedWriter writer, String name, Set<Type> typeList) throws IOException {
		int rootId = ROOT_PARENT + 1;
		int nodeCount = ROOT_PARENT + 1;
		writeEntityArguments(writer, ConstantManager.NODE_ENTITY_NAME, ROOT_PARENT, name);
		writer.write(newLine);

		// Write types first
		String typeEntityName = ConstantManager.TYPE_ENTITY_NAME;
		for (Type type : typeList) {
			// Write type and increase node count
			writeField(writer, type, typeEntityName, rootId);
			nodeCount++;

			// Write type components
			nodeCount = writeNode(writer, type, nodeCount, nodeCount);
		}

		return nodeCount;
	}

	private void writeField(BufferedWriter writer, Field field, String entityString, int parentId) throws IOException {
		writeEntityArguments(writer, entityString, parentId, field.getName());
		writer.write(field.getType().toString());
		writer.write(newLine);
	}

	private int writeNode(BufferedWriter writer, Type node, int parentId, int nodeCount) throws IOException {
		for (Field field : node.getFieldSet()) {
			boolean isNode = field instanceof Node;
			String entityString = ConstantManager.NODE_ENTITY_NAME;
			if (!isNode) {
				entityString = ConstantManager.PARAMETER_ENTITY_NAME;
			}

			writeField(writer, field, entityString, parentId);

			if (isNode) {
				Type innerNode = (Type) field;
				int innerNodeId = nodeCount + 1;
				nodeCount = writeNode(writer, innerNode, innerNodeId, innerNodeId);
				for (Constraint constraint : innerNode.getConstraintSet()) {
					writeConstraint(writer, constraint, innerNodeId);
				}
			}
		}

		return nodeCount;
	}

	private void writeConstraint(BufferedWriter writer, Constraint constraint, int parentId) throws IOException {
		writeEntityArguments(writer, ConstantManager.CONSTRAINT_ENTITY_NAME, parentId, constraint.getName());
		writer.write(constraint.parametersToString().strip());
		writer.write(newLine);
	}

	public void saveProject() throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(projectFile))) {
			// Write root node
			int nodeCount = writeRoot(writer, projectRoot.getName(), projectRoot.getTypeList());
			writeNode(writer, projectRoot, ROOT_PARENT + 1, nodeCount);
		}
	}

	public boolean createProject(String projectName) throws IOException {
		projectName = projectName.strip();

		// Name must finish by .taf
		if (!projectName.endsWith(ConstantManager.TAF_FILE_EXTENSION)) {
			return false;
		}

		File newProjectFile = getProjectFileFromName(projectName);
		if (newProjectFile.exists()) {
			return false;
		}

		newProjectFile.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(newProjectFile));
		// Write an empty root with no type
		writeRoot(writer, projectName.substring(0, projectName.length() - ConstantManager.TAF_FILE_EXTENSION.length()),
				new HashSet<Type>());
		writer.close();

		// Add to created project
		projectNames.add(newProjectFile);

		return true;
	}

	public void closeProject(boolean save) throws IOException {
		if (save) {
			saveProject();
		}

		projectFile = null;
		projectRoot = null;

		// Remove all custom types
		TypeManager.getInstance().resetCustomNodeTypes();

		// Send close event
		EventManager.getInstance().fireEvent(new ProjectClosedEvent());
	}

	void exportToXML(File xmlFile) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFile))) {
			writer.write(XML_HEADER);
			writer.write(projectRoot.toString());
			writer.write(newLine);
		}
	}

	void exportToXML(boolean askCustomLocation, boolean save) throws IOException {
		if (projectRoot == null || projectFile == null) {
			return;
		}

		if (save) {
			saveProject();
		}

		String projectFileName = projectFile.getName();
		String projectName = projectFileName.substring(0,
				projectFileName.length() - ConstantManager.TAF_FILE_EXTENSION.length());
		String xmlFileName = projectName + ConstantManager.XML_FILE_EXTENSION;

		File homeFile = FileSystemView.getFileSystemView().getHomeDirectory();
		File predictedFile = new File(homeFile.getAbsolutePath() + File.separator + xmlFileName);

		File exportFile = new File(runDirectory + File.separator + projectName + File.separator + xmlFileName);
		if (askCustomLocation) {
			// Show file chooser in save mode
			JFileChooser chooser = new JFileChooser();
			chooser.setSelectedFile(predictedFile);
			int returnVal = chooser.showSaveDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File xmlFile = chooser.getSelectedFile();
				if (xmlFile.exists()) {
					int answer = JOptionPane.showConfirmDialog(null,
							"This file already exist, do you want to replace it?", "File already exist",
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if (answer != JOptionPane.YES_OPTION) {
						return;
					}
				}
				exportFile = xmlFile;
			}
		}

		exportToXML(exportFile);
	}

	public void exportToXML(boolean save) throws IOException {
		exportToXML(true, save);
	}

	String getMainDirectoryPath() {
		return mainDirectory;
	}

	private File getCustomLocation(File customLocationFile) throws IOException {
		File newLocationFile = null;
		try (BufferedReader reader = new BufferedReader(new FileReader(customLocationFile))) {
			String newLocationPath = reader.readLine();
			File newLocation = new File(newLocationPath);
			if (!newLocation.exists()) {
				ConstantManager.showError("Custom location does not exist, using default location...");
			} else {
				newLocationFile = newLocation;
			}
		}

		return newLocationFile;
	}

	private void copyDirectory(Path sourcePath, Path destinationPath) throws IOException {
		Files.walk(sourcePath).forEach(source -> {
			Path destination = Paths.get(destinationPath.toString(),
					source.toString().substring(sourcePath.toString().length()));
			try {
				Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING,
						StandardCopyOption.COPY_ATTRIBUTES);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	File getProjectRunFolder() {
		String projectFileName = projectFile.getName();
		String projectName = projectFile.getName().substring(0,
				projectFileName.length() - ConstantManager.TAF_FILE_EXTENSION.length());
		File projectRunFolder = new File(runDirectory + File.separator + projectName);
		File customLocationFile = new File(projectRunFolder.getAbsolutePath() + File.separator + "custom_location.txt");

		if (!projectRunFolder.exists()) {
			projectRunFolder.mkdir();

			// Ask if the user wants a custom location
			int answer = JOptionPane.showConfirmDialog(null, "Do you want to run in a custom location?",
					"Custom location", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (answer == JOptionPane.YES_OPTION) {
				// Show a file chooser and select a directory!
				JFileChooser projectLocationChooser = new JFileChooser();
				projectLocationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				projectLocationChooser.setMultiSelectionEnabled(false);
				answer = projectLocationChooser.showDialog(null, "Select location");

				if (answer == JFileChooser.APPROVE_OPTION) {
					File newLocation = projectLocationChooser.getSelectedFile();

					if (!newLocation.exists()) {
						newLocation.mkdir();
					}

					try (BufferedWriter writer = new BufferedWriter(new FileWriter(customLocationFile))) {
						writer.write(newLocation.getAbsolutePath());
					} catch (IOException e) {
						ConstantManager.showError(
								"An error occured when creating the custom location file: " + e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}

		// Check the existence of the custom location file
		if (customLocationFile.exists()) {
			try {
				projectRunFolder = getCustomLocation(customLocationFile);
			} catch (IOException e) {
				ConstantManager.showError(
						"An error occured when reading the custom location file. Using default location instead\n"
								+ e.getMessage());
				e.printStackTrace();
			}
		}

		return projectRunFolder;
	}

	public void removeRunCustomLocation(boolean wantCopy) throws IOException {
		String projectFileName = projectFile.getName();
		String projectName = projectFile.getName().substring(0,
				projectFileName.length() - ConstantManager.TAF_FILE_EXTENSION.length());
		File projectRunFolder = new File(runDirectory + File.separator + projectName);
		File customLocationFile = new File(projectRunFolder.getAbsolutePath() + File.separator + "custom_location.txt");

		// If user wants, copy from the old one to here
		if (wantCopy) {
			File oldLocationFile = getCustomLocation(customLocationFile);
			copyDirectory(oldLocationFile.toPath(), projectRunFolder.toPath());
		}

		if (customLocationFile.exists()) {
			customLocationFile.delete();
		}
	}

	public void changeRunCustomLocation(String customLocationPath, boolean wantCopy) throws IOException {
		String projectFileName = projectFile.getName();
		String projectName = projectFile.getName().substring(0,
				projectFileName.length() - ConstantManager.TAF_FILE_EXTENSION.length());
		File projectRunFolder = new File(runDirectory + File.separator + projectName);
		File customLocationFile = new File(projectRunFolder.getAbsolutePath() + File.separator + "custom_location.txt");

		// If user wants to copy, check if the files are in the default directory first
		if (wantCopy) {
			File newLocation = new File(customLocationPath);
			if (customLocationFile.exists()) {
				File oldLocation = getCustomLocation(customLocationFile);
				copyDirectory(oldLocation.toPath(), newLocation.toPath());
			} else {
				copyDirectory(projectRunFolder.toPath(), newLocation.toPath());
			}
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(customLocationFile))) {
			writer.write(customLocationPath);
		}
	}

	String getRunXMLFileName() {
		String projectFileName = projectFile.getName();
		String projectName = projectFile.getName().substring(0,
				projectFileName.length() - ConstantManager.TAF_FILE_EXTENSION.length());
		String xmlFileName = projectName + ConstantManager.XML_FILE_EXTENSION;
		return xmlFileName;
	}

	public void deleteProject(String projectName) {
		File fileToDelete = getProjectFileFromName(projectName);
		if (!projectNames.contains(fileToDelete)) {
			return;
		}

		// Remove from the set of names and delete file
		projectNames.remove(fileToDelete);
		fileToDelete.delete();
	}

	public String importProject(File xmlTafFile) throws ImportException {
		// TODO Check if associated taf file exist
		String newTafFileName = xmlTafFile.getName().replace(ConstantManager.XML_FILE_EXTENSION,
				ConstantManager.TAF_FILE_EXTENSION);
		File newTafFile = getProjectFileFromName(newTafFileName);

		XMLTafReader xmlReader = new XMLTafReader(xmlTafFile);
		try {
			// Get the lines from the XML file and write them
			String convertedLines = xmlReader.readFile();
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(newTafFile))) {
				writer.write(convertedLines);
			}

			// Open the project to verify that everything is parsable
			openProject(newTafFileName);

			// Add to files
			projectNames.add(newTafFile);
		} catch (ImportException | IOException | ParseException e) {
			if (newTafFile.exists()) {
				newTafFile.delete();
			}
			throw new ImportException(SaveManager.class, IMPORT_EXCEPTION_ERROR_MESSAGE + e.getMessage());
		}

		// Return the new file name
		return newTafFileName;
	}

	private static void throwParseException(String message, int lineNumber) throws ParseException {
		throw new ParseException(SaveManager.class, PARSE_EXCEPTION_FORMAT_ERROR_MESSAGE.formatted(message, lineNumber));
	}

	public String[] getProjectNames() {
		return projectNames.stream().map(file -> file.getName()).toArray(String[]::new);
	}

	public static SaveManager getInstance() {
		return instance;
	}

}
