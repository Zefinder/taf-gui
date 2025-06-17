package com.taf.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import com.taf.exception.ParseException;
import com.taf.logic.constraint.Constraint;
import com.taf.logic.constraint.parameter.ConstraintParameter;
import com.taf.logic.constraint.parameter.ConstraintParameterFactory;
import com.taf.logic.field.Field;
import com.taf.logic.field.Node;
import com.taf.logic.field.Parameter;
import com.taf.logic.field.Root;
import com.taf.logic.type.AnonymousType;
import com.taf.logic.type.BooleanType;
import com.taf.logic.type.IntegerType;
import com.taf.logic.type.RealType;
import com.taf.logic.type.StringType;
import com.taf.logic.type.Type;
import com.taf.logic.type.parameter.TypeParameter;
import com.taf.logic.type.parameter.TypeParameterFactory;
import com.taf.logic.type.parameter.TypeParameterFactory.MinMaxTypeParameterType;
import com.taf.util.OSValidator;

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

	private static final String SAVE_ARGUMENT_FORMAT = "%s=\"([^\"]+)\"";
	private static final String ENTITY_ARGUMENT = "entity";
	private static final String PARENT_ARGUMENT = "parent";
	private static final String NAME_ARGUMENT = "name";
	private static final String INSTANCE_ARGUMENT = "nb_instances";
	private static final String MIN_INSTANCE_ARGUMENT = "min";
	private static final String MAX_INSTANCE_ARGUMENT = "max";
	private static final String TYPE_ARGUMENT = "type";

	private static final int ROOT_PARENT = -1;

	private static final String ENTITY_MISSING_ERROR_MESSAGE = "The entity must have a type!";
	private static final String PARENT_MISSING_ERROR_MESSAGE = "The entity must have a parent!";
	private static final String PARENT_UNKNWON_FORMAT_ERROR_MESSAGE = "The parent %d does not exist!";
	private static final String NAME_MISSING_ERROR_MESSAGE = "Entity must have a name!";
	private static final String INSTANCE_MISSING_ERROR_MESSAGE = "Node must have either an instance number or a minimum and a maximum number";
	private static final String PARAMETER_TYPE_ERROR_MESSAGE = "Parameter must have a type";
	private static final String PARAMETER_UNEXPECTED_ERROR_MESSAGE = "Unexpected parameter type value: ";
	private static final String PARAMETER_TYPE_MISSING_FORMAT_ERROR_MESSAGE = "Type %s must have the \"%s\" argument";
	private static final String NUMBER_FORMAT_EXCEPTION_ERROR_MESSAGE = "An argument value was not a number (no more information)";
	private static final String PARSE_EXCEPTION_FORMAT_ERROR_MESSAGE = "%s (line %d)";

	private static final SaveManager instance = new SaveManager();

	private final Set<File> projectNames;
	private final OSValidator OS;

	private String mainDirectory;
	private File mainDirectoryFile;

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

		// TODO Tell the user that a directory will be created
		mainDirectory += File.separator + TAF_DIRECTORY_NAME;
		mainDirectoryFile = new File(mainDirectory);

		// Create all directories if do not exist
		initCreation();

		// Register all project files
		initProjectFiles();
	}

	private void initCreation() {
		if (!mainDirectoryFile.exists()) {
			mainDirectoryFile.mkdirs();
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
		List<Node> nodes = new ArrayList<Node>();

		try (BufferedReader reader = new BufferedReader(new FileReader(projectFile))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// If line is empty, skip
				if (line.isBlank()) {
					continue;
				}

				// Get entity type
				Optional<String> entityTypeOp = getArgument(line, ENTITY_ARGUMENT);
				if (entityTypeOp.isEmpty()) {
					new ParseException(ENTITY_MISSING_ERROR_MESSAGE);
				}
				String entityType = entityTypeOp.get();

				// Get parent id
				Optional<String> parentIdOp = getArgument(line, PARENT_ARGUMENT);
				if (parentIdOp.isEmpty()) {
					new ParseException(PARENT_MISSING_ERROR_MESSAGE);
				}
				int parentId = Integer.valueOf(parentIdOp.get());

				// Check if parent exists
				if (parentId < -1 || parentId >= nodes.size()) {
					new ParseException(PARENT_UNKNWON_FORMAT_ERROR_MESSAGE.formatted(parentId));
				}

				// TODO Check if multiple roots

				// Search for the name
				Optional<String> entityNameOp = getArgument(line, NAME_ARGUMENT);
				if (entityNameOp.isEmpty()) {
					new ParseException(NAME_MISSING_ERROR_MESSAGE);
				}
				String entityName = entityNameOp.get();

				switch (entityType) {
				case ConstantManager.NODE_ENTITY_NAME:
					Node node;

					if (parentId == -1) {
						// This is the root node
						root = new Root(entityName);
						node = root;

					} else {
						AnonymousType type = new AnonymousType();

						// Check if number of instances or min-max, else throw
						Optional<String> instanceNumberOp = getArgument(line, INSTANCE_ARGUMENT);
						if (instanceNumberOp.isEmpty()) {
							Optional<String> minNumberOp = getArgument(line, MIN_INSTANCE_ARGUMENT);
							Optional<String> maxNumberOp = getArgument(line, MAX_INSTANCE_ARGUMENT);

							if (minNumberOp.isEmpty() || maxNumberOp.isEmpty()) {
								throw new ParseException(INSTANCE_MISSING_ERROR_MESSAGE);
							}

							int minNumber = Integer.valueOf(minNumberOp.get());
							int maxNumber = Integer.valueOf(maxNumberOp.get());
							type.addMinMaxInstanceParameter(minNumber, maxNumber);

						} else {
							int instanceNumber = Integer.valueOf(instanceNumberOp.get());
							type.editInstanceNumberParameter(instanceNumber);
						}

						node = new Node(entityName, type);
						nodes.get(parentId).addField(node);
					}

					nodes.add(node);
					break;

				case ConstantManager.PARAMETER_ENTITY_NAME:
					// Get parameter type
					Optional<String> typeNameOp = getArgument(line, TYPE_ARGUMENT);
					if (typeNameOp.isEmpty()) {
						new ParseException(PARAMETER_TYPE_ERROR_MESSAGE);
					}
					String typeName = typeNameOp.get();
					TypeParameterFactory.MinMaxTypeParameterType maxTypeParameterType;
					Type type = switch (typeName) {
					case BooleanType.TYPE_NAME: {
						maxTypeParameterType = MinMaxTypeParameterType.NONE;
						yield new BooleanType();
					}

					case IntegerType.TYPE_NAME: {
						maxTypeParameterType = MinMaxTypeParameterType.INTEGER;
						yield new IntegerType();
					}

					case RealType.TYPE_NAME: {
						maxTypeParameterType = MinMaxTypeParameterType.REAL;
						yield new RealType();
					}

					case StringType.TYPE_NAME: {
						maxTypeParameterType = MinMaxTypeParameterType.NONE;
						yield new StringType();
					}

					default:
						throw new ParseException(PARAMETER_UNEXPECTED_ERROR_MESSAGE + typeName);
					};

					for (String mandatoryTypeName : type.getMandatoryParametersName()) {
						Optional<String> mandatoryTypeOp = getArgument(line, mandatoryTypeName);
						if (mandatoryTypeOp.isEmpty()) {
							throw new ParseException(
									PARAMETER_TYPE_MISSING_FORMAT_ERROR_MESSAGE.formatted(typeName, mandatoryTypeName));
						}
						String typeParameterValue = mandatoryTypeOp.get();

						TypeParameter typeParameter = TypeParameterFactory.createTypeParameter(mandatoryTypeName,
								typeParameterValue, maxTypeParameterType);
						type.addTypeParameter(typeParameter);
					}

					// TODO Create function to add to type
					for (String optionalTypeName : type.getOptionalParametersName()) {
						Optional<String> optionalTypeOp = getArgument(line, optionalTypeName);
						if (!optionalTypeOp.isEmpty()) {
							String typeParameterValue = optionalTypeOp.get();

							TypeParameter typeParameter = TypeParameterFactory.createTypeParameter(optionalTypeName,
									typeParameterValue, maxTypeParameterType);
							type.addTypeParameter(typeParameter);
						}
					}

					Parameter parameter = new Parameter(entityName, type);
					nodes.get(parentId).addField(parameter);
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

					nodes.get(parentId).addConstraint(constraint);
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

	private void writeEntityArguments(BufferedWriter writer, String entityString, int nodeId, String name)
			throws IOException {
		writer.write(format.formatted(ENTITY_ARGUMENT, entityString));
		writer.write(format.formatted(PARENT_ARGUMENT, nodeId));
		writer.write(format.formatted(NAME_ARGUMENT, name));
	}

	private void writeRoot(BufferedWriter writer, String name) throws IOException {
		writeEntityArguments(writer, ConstantManager.NODE_ENTITY_NAME, ROOT_PARENT, name);
		writer.write(newLine);
	}

	private void writeField(BufferedWriter writer, Field field, String entityString, int nodeId) throws IOException {
		writeEntityArguments(writer, entityString, nodeId, field.getName());
		writer.write(field.getType().toString());
		writer.write(newLine);
	}

	private int writeNode(BufferedWriter writer, Node node, int nodeId) throws IOException {
		int numberNodes = nodeId;
		for (Field field : node.getFieldList()) {
			boolean isNode = field instanceof Node;
			String entityString = ConstantManager.NODE_ENTITY_NAME;
			if (!isNode) {
				entityString = ConstantManager.PARAMETER_ENTITY_NAME;
			}

			writeField(writer, field, entityString, nodeId);

			if (isNode) {
				Node innerNode = (Node) field;
				int innerNodeId = numberNodes + 1;
				numberNodes = writeNode(writer, innerNode, innerNodeId);
				for (Constraint constraint : innerNode.getConstraintList()) {
					writeConstraint(writer, constraint, innerNodeId);
				}
			}
		}
		
		return numberNodes;
	}

	private void writeConstraint(BufferedWriter writer, Constraint constraint, int nodeId) throws IOException {
		writeEntityArguments(writer, ConstantManager.CONSTRAINT_ENTITY_NAME, nodeId, constraint.getName());
		writer.write(constraint.parametersToString().strip());
		writer.write(newLine);
	}

	public void saveProject() throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(projectFile))) {
			// Write root node
			writeRoot(writer, projectRoot.getName());
			writer.write(newLine);
			writeNode(writer, projectRoot, ROOT_PARENT + 1);
		}
	}

	public boolean createProject(String projectName) throws IOException {
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
		writeRoot(writer, projectName.substring(0, projectName.length() - ConstantManager.TAF_FILE_EXTENSION.length()));
		writer.close();

		return true;
	}

	public void exportToXML() throws IOException {
		if (projectRoot == null || projectFile == null) {
			return;
		}

		String projectFileName = projectFile.getName();
		String xmlFileName = projectFileName.substring(0,
				projectFileName.length() - ConstantManager.TAF_FILE_EXTENSION.length())
				+ ConstantManager.XML_FILE_EXTENSION;

		File homeFile = FileSystemView.getFileSystemView().getHomeDirectory();
		File predictedFile = new File(homeFile.getAbsolutePath() + File.separator + xmlFileName);

		// Show file chooser in save mode
		JFileChooser chooser = new JFileChooser();
		chooser.setSelectedFile(predictedFile);
		int returnVal = chooser.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// TODO Verify if file exists
			File xmlFile = chooser.getSelectedFile();
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFile))) {
				writer.write(XML_HEADER);
				writer.write(projectRoot.toString());
				writer.write(newLine);
			}
		}
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
	
	public void importProject(File xmlTafFile) {
		
	}

	private static void throwParseException(String message, int lineNumber) throws ParseException {
		throw new ParseException(PARSE_EXCEPTION_FORMAT_ERROR_MESSAGE.formatted(message, lineNumber));
	}

	public String[] getProjectNames() {
		return projectNames.stream().map(file -> file.getName()).toArray(String[]::new);
	}

	public static SaveManager getInstance() {
		return instance;
	}

}
