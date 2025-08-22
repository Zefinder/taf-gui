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
import com.taf.exception.EntityCreationException;
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
import com.taf.util.Consts;
import com.taf.util.OSValidator;
import com.taf.util.XMLTafReader;

/**
 * <p>
 * The SaveManager manager focuses on saving and reading TAF project files. The
 * file syntax is easy to understand:
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
 *
 * @author Adrien Jakubiak
 */
public class SaveManager extends Manager {

	/** The xml header. */
	private static final String XML_HEADER = "<?xml version=\"1.0\"?>\n\n\n";

	/** The parameter separator. */
	private static final String separator = Consts.PARAMETER_SEPARATOR;

	/** The new line character. */
	private static final String newLine = Consts.LINE_JUMP;

	/** The parameter string format. */
	private static final String format = Consts.PARAMETER_STRING_FORMAT + separator;

	/** The windows user data directory. */
	static final String WINDOWS_USER_BASE_MAIN_DIR = System.getenv("APPDATA");

	/** The unix mac user data directory. */
	static final String UNIX_MAC_USER_BASE_MAIN_DIR = System.getProperty("user.home");

	/** The other user data directory. */
	static final String OTHER_USER_BASE_MAIN_DIR = ".";

	/** The TAF directory name. */
	static final String TAF_DIRECTORY_NAME = "tafgui";

	/** The TAF run directory name. */
	static final String RUN_DIRECTORY_NAME = "run";

	/** The save argument format. */
	private static final String SAVE_ARGUMENT_FORMAT = "\\b%s=\"([^\"]+)\""; // Arguments must be separated

	/** The entity argument name in the save file. */
	private static final String ENTITY_ARGUMENT = "entity";

	/** The parent argument name in the save file. */
	private static final String PARENT_ARGUMENT = "parent";

	/** The name argument name in the save file. */
	private static final String NAME_ARGUMENT = "name";

	/** The type argument name in the save file. */
	private static final String TYPE_ARGUMENT = "type";

	/** The root parent id. */
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

	/** The manager instance. */
	private static final SaveManager instance = new SaveManager();

	/**
	 * Gets the single instance of SaveManager.
	 *
	 * @return single instance of SaveManager
	 */
	public static SaveManager getInstance() {
		return instance;
	}

	/**
	 * Copy a directory from a location to another.
	 *
	 * @param sourcePath      the source path
	 * @param destinationPath the destination path
	 * @throws IOException if the copy failed.
	 */
	private static void copyDirectory(Path sourcePath, Path destinationPath) throws IOException {
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

	/**
	 * Throws a parse exception with a message and a line number. This is made to be
	 * displayed to the user.
	 *
	 * @param message    the message to display
	 * @param lineNumber the line number
	 * @throws ParseException a parse exception gathering all previous messages
	 */
	private static void throwParseException(String message, int lineNumber) throws ParseException {
		throw new ParseException(SaveManager.class,
				PARSE_EXCEPTION_FORMAT_ERROR_MESSAGE.formatted(message, lineNumber));
	}

	/** The registered project names. */
	private final Set<File> projectNames;

	/** The running OS. */
	private final OSValidator OS;

	/** The user data directory. */
	private String mainDirectory;

	/** The user data directory file. */
	private File mainDirectoryFile;

	/** The TAF run directory. */
	private String runDirectory;

	/** The TAF run directory file. */
	private File runDirectoryFile;

	/** The opened project file. */
	private File projectFile;

	/** The opened project root. */
	private Root projectRoot;

	private SaveManager() {
		projectNames = new HashSet<File>();
		OS = OSValidator.getOS();
	}

	/**
	 * Changes the run directory location for the opened project. The user may want
	 * to copy its settings to the new location.
	 *
	 * @param customLocationPath the custom location path
	 * @param wantCopy           true if the directory content must be copied from
	 *                           the old location
	 * @throws IOException if an error happens when copying the files
	 */
	public void changeRunCustomLocation(String customLocationPath, boolean wantCopy) throws IOException {
		String projectFileName = projectFile.getName();
		String projectName = projectFile.getName().substring(0,
				projectFileName.length() - Consts.TAF_FILE_EXTENSION.length());
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

	@Override
	public void clearManager() {
		// Nothing to do here
	}

	/**
	 * Closes the current project. It will remove all the custom types and
	 * references from the type manger and save the project if the user wants to.
	 *
	 * @param save true if the project needs to be saved before closing
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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

	/**
	 * Creates a new project with a custom project name. If the project name already
	 * exists, then it returns false.
	 *
	 * @param projectName the project name
	 * @return true if the project has been created
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean createProject(String projectName) throws IOException {
		projectName = projectName.strip();

		// Name must finish by .taf
		if (!projectName.endsWith(Consts.TAF_FILE_EXTENSION)) {
			return false;
		}

		File newProjectFile = getProjectFileFromName(projectName);
		if (newProjectFile.exists()) {
			return false;
		}

		newProjectFile.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(newProjectFile));
		// Write an empty root with no type
		writeRoot(writer, projectName.substring(0, projectName.length() - Consts.TAF_FILE_EXTENSION.length()),
				new HashSet<Type>());
		writer.close();

		// Add to created project
		projectNames.add(newProjectFile);

		return true;
	}

	/**
	 * Deletes the selected project.
	 *
	 * @param projectName the project name
	 */
	public void deleteProject(String projectName) {
		File fileToDelete = getProjectFileFromName(projectName);
		if (!projectNames.contains(fileToDelete)) {
			return;
		}

		// Remove from the set of names and delete file
		projectNames.remove(fileToDelete);
		fileToDelete.delete();
	}

	/**
	 * Export the current project to XML.
	 *
	 * @param save true if the project must be saved before exporting
	 * @throws IOException if an error happens during the export
	 */
	public void exportToXML(boolean save) throws IOException {
		exportToXML(true, save);
	}

	/**
	 * Returns the registered project names.
	 *
	 * @return the registered project names
	 */
	public String[] getProjectNames() {
		return projectNames.stream().map(file -> file.getName()).toArray(String[]::new);
	}

	/**
	 * Import a new project in the XML format. If the file already exist, then
	 * returns null.
	 *
	 * @param xmlTafFile the XML TAF file
	 * @return the name of the file, or null if the file already existed.
	 * @throws ImportException if the XML file contained errors
	 */
	// TODO Change return value to boolean
	public String importProject(File xmlTafFile) throws ImportException {
		// TODO Check if associated taf file exist
		String newTafFileName = xmlTafFile.getName().replace(Consts.XML_FILE_EXTENSION, Consts.TAF_FILE_EXTENSION);
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

	/**
	 * Opens the project with the specified file. The save file syntax is defined in
	 * {@link SaveManager}. It returns the root of the project, containing all nodes
	 * and parameters.
	 *
	 * @param projectName the project name
	 * @return the project root.
	 * @throws IOException    if an error occurred when reading the file
	 * @throws ParseException if the save file is not parsable
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
				case Consts.TYPE_ENTITY_NAME:
					Type type = new Type(entityName);

					// Parent id must be 0
					// TODO send a warning

					typeList.get(0).addEntity(type);
					typeList.add(type);

					// Add to type manager
					TypeManager.getInstance().addCustomNodeType(type);
					break;

				case Consts.NODE_ENTITY_NAME:
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

				case Consts.PARAMETER_ENTITY_NAME:
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

				case Consts.CONSTRAINT_ENTITY_NAME:
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
		} catch (ParseException e) {
			throwParseException(e.getMessage(), lineNumber);
		} catch (EntityCreationException e) {
			throwParseException(e.getMessage(), lineNumber);
		}

		this.projectRoot = root;
		return root;
	}

	/**
	 * Resets the run location to the default run directory.
	 *
	 * @param wantCopy true if the user wants to copy its settings to the default
	 *                 location
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void removeRunCustomLocation(boolean wantCopy) throws IOException {
		String projectFileName = projectFile.getName();
		String projectName = projectFile.getName().substring(0,
				projectFileName.length() - Consts.TAF_FILE_EXTENSION.length());
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

	/**
	 * Saves the opened project.
	 *
	 * @throws IOException if the save file couldn't be written
	 */
	public void saveProject() throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(projectFile))) {
			// Write root node
			int nodeCount = writeRoot(writer, projectRoot.getName(), projectRoot.getTypeSet());
			writeNode(writer, projectRoot, ROOT_PARENT + 1, nodeCount);
		}
	}

	/**
	 * Export the current project to XML. The user can want to export it to a custom
	 * location and/or save the project before export.
	 *
	 * @param askCustomLocation true if the file is in a custom location
	 * @param save              true if the project must be saved
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// TODO Move the JFileChooser out of the manager
	void exportToXML(boolean askCustomLocation, boolean save) throws IOException {
		if (projectRoot == null || projectFile == null) {
			return;
		}

		if (save) {
			saveProject();
		}

		String projectFileName = projectFile.getName();
		String projectName = projectFileName.substring(0,
				projectFileName.length() - Consts.TAF_FILE_EXTENSION.length());
		String xmlFileName = projectName + Consts.XML_FILE_EXTENSION;

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

	/**
	 * Export the opened project to the specified XML file.
	 *
	 * @param xmlFile the xml file
	 * @throws IOException if the file couldn't be written
	 */
	void exportToXML(File xmlFile) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFile))) {
			writer.write(XML_HEADER);
			writer.write(projectRoot.toString());
			writer.write(newLine);
		}
	}

	/**
	 * Returns the main directory path.
	 *
	 * @return the main directory path
	 */
	String getMainDirectoryPath() {
		return mainDirectory;
	}

	/**
	 * Returns the opened project run folder.
	 *
	 * @return the opened project run folder
	 */
	File getProjectRunFolder() {
		String projectFileName = projectFile.getName();
		String projectName = projectFile.getName().substring(0,
				projectFileName.length() - Consts.TAF_FILE_EXTENSION.length());
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
						Consts.showError("An error occured when creating the custom location file: " + e.getMessage());
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
				Consts.showError(
						"An error occured when reading the custom location file. Using default location instead\n"
								+ e.getMessage());
				e.printStackTrace();
			}
		}

		return projectRunFolder;
	}

	/**
	 * Returns the XML file name for the project to run.
	 *
	 * @return the run XML file name
	 */
	String getRunXMLFileName() {
		String projectFileName = projectFile.getName();
		String projectName = projectFile.getName().substring(0,
				projectFileName.length() - Consts.TAF_FILE_EXTENSION.length());
		String xmlFileName = projectName + Consts.XML_FILE_EXTENSION;
		return xmlFileName;
	}

	/**
	 * Adds the parameters to field type.
	 *
	 * @param fieldType  the field type
	 * @param line       the line to read
	 * @param minMaxType the minimum-maximum type
	 * @throws ParseException if the mandatory parameter is absent or if the type
	 *                        parameter could not be created
	 */
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

	/**
	 * Returns the argument value from a line using the argument name.
	 *
	 * @param line         the line
	 * @param argumentName the argument name
	 * @return the argument
	 */
	private Optional<String> getArgument(String line, String argumentName) {
		Pattern argumentPattern = Pattern.compile(SAVE_ARGUMENT_FORMAT.formatted(argumentName));
		Matcher m = argumentPattern.matcher(line);
		if (m.find()) {
			return Optional.of(m.group(1));
		}

		return Optional.empty();
	}

	/**
	 * Returns the custom run location, or the default location if the pointed
	 * location does not exist.
	 *
	 * @param customLocationFile the custom location file
	 * @return the custom location
	 * @throws IOException if the location file couldn't be read.
	 */
	private File getCustomLocation(File customLocationFile) throws IOException {
		File newLocationFile = null;
		try (BufferedReader reader = new BufferedReader(new FileReader(customLocationFile))) {
			String newLocationPath = reader.readLine();
			File newLocation = new File(newLocationPath);
			if (!newLocation.exists()) {
				Consts.showError("Custom location does not exist, using default location...");
			} else {
				newLocationFile = newLocation;
			}
		}

		return newLocationFile;
	}

	/**
	 * Returns the project file from a project name.
	 *
	 * @param projectName the project name
	 * @return the project file from name
	 */
	private File getProjectFileFromName(String projectName) {
		return new File(mainDirectory + File.separator + projectName);
	}

	/**
	 * Initializes the directories.
	 */
	private void initCreation() {
		if (!mainDirectoryFile.exists()) {
			mainDirectoryFile.mkdirs();
		}

		if (!runDirectoryFile.exists()) {
			runDirectoryFile.mkdir();
		}
	}

	/**
	 * Initializes the project files.
	 */
	private void initProjectFiles() {
		File[] tafFiles = mainDirectoryFile.listFiles((dir, name) -> name.endsWith(Consts.TAF_FILE_EXTENSION));
		for (File tafFile : tafFiles) {
			projectNames.add(tafFile);
		}
	}

	/**
	 * Write a constraint to an entity.
	 *
	 * @param writer     the writer
	 * @param constraint the constraint
	 * @param parentId   the parent id
	 * @throws IOException if the constraint couldn't be written.
	 */
	private void writeConstraint(BufferedWriter writer, Constraint constraint, int parentId) throws IOException {
		writeEntityArguments(writer, Consts.CONSTRAINT_ENTITY_NAME, parentId, constraint.getName());
		writer.write(constraint.parametersToString().strip());
		writer.write(newLine);
	}

	/**
	 * Write entity arguments.
	 *
	 * @param writer       the writer
	 * @param entityString the entity string
	 * @param nodeId       the node id
	 * @param name         the name
	 * @throws IOException if the arguments couldn't be written.
	 */
	private void writeEntityArguments(BufferedWriter writer, String entityString, int nodeId, String name)
			throws IOException {
		writer.write(format.formatted(ENTITY_ARGUMENT, entityString));
		writer.write(format.formatted(PARENT_ARGUMENT, nodeId));
		writer.write(format.formatted(NAME_ARGUMENT, name));
	}

	/**
	 * Write a field.
	 *
	 * @param writer       the writer
	 * @param field        the field
	 * @param entityString the entity string
	 * @param parentId     the parent id
	 * @throws IOException if the field couldn't be written.
	 */
	private void writeField(BufferedWriter writer, Field field, String entityString, int parentId) throws IOException {
		writeEntityArguments(writer, entityString, parentId, field.getName());
		writer.write(field.getType().toString());
		writer.write(newLine);
	}

	/**
	 * Write a node.
	 *
	 * @param writer    the writer
	 * @param node      the node
	 * @param parentId  the parent id
	 * @param nodeCount the node count
	 * @return the current node count
	 * @throws IOException if the node couldn't be written.
	 */
	private int writeNode(BufferedWriter writer, Type node, int parentId, int nodeCount) throws IOException {
		for (Field field : node.getFieldSet()) {
			boolean isNode = field instanceof Node;
			String entityString = Consts.NODE_ENTITY_NAME;
			if (!isNode) {
				entityString = Consts.PARAMETER_ENTITY_NAME;
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

	/**
	 * Write the root node.
	 *
	 * @param writer   the writer
	 * @param name     the name
	 * @param typeList the type list
	 * @return the current node count
	 * @throws IOException if the root node couldn't be written.
	 */
	private int writeRoot(BufferedWriter writer, String name, Set<Type> typeList) throws IOException {
		int rootId = ROOT_PARENT + 1;
		int nodeCount = ROOT_PARENT + 1;
		writeEntityArguments(writer, Consts.NODE_ENTITY_NAME, ROOT_PARENT, name);
		writer.write(newLine);

		// Write types first
		String typeEntityName = Consts.TYPE_ENTITY_NAME;
		for (Type type : typeList) {
			// Write type and increase node count
			writeField(writer, type, typeEntityName, rootId);
			nodeCount++;

			// Write type components
			nodeCount = writeNode(writer, type, nodeCount, nodeCount);
		}

		return nodeCount;
	}

}
