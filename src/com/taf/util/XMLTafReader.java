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
package com.taf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taf.exception.ImportException;
import com.taf.exception.RangeException;

/**
 * The XMLTafReader class allows parsing from a TAF XML file to a TAF save file
 * format. It uses a {@link RangeTree} to register nodes and types.
 * 
 * @see RangeTree
 *
 * @author Adrien Jakubiak
 */
public class XMLTafReader {

	private static final String FILE_READ_ERROR_MESSAGE = "Something happened when trying to read the file to import: ";

	private static final String NO_ROOT_ERROR_MESSAGE = "There is no root in the imported project!";

	private static final String NODES_ENTER_EXIT_NUMBER_MISSMATCH_ERROR_MESSAGE = "The number of <node> and </node> is different!";

	private static final String NODES_EXIT_AFTER_ENTER_ERROR_MESSAGE = "Element </node> is before <node>!";

	private static final String RANGE_ERROR_MESSAGE = "Error when adding the position range: ";

	/** String pattern to get the root in a TAF XML file. */
	private static final String ROOT_PATTERN_STRING = "<root((?s).+?)>((?s).+?)</root>";

	/** String pattern to get a node with its parameters in a TAF XML file. */
	private static final String NODE_ENTER_PATTERN_STRING = "<node((?s).+?)>";

	/** String pattern that closes a node in a TAF XML file. */
	private static final String NODE_EXIT_PATTERN_STRING = "</node>";

	/** String pattern to get a type with its parameters in a TAF XML file. */
	private static final String TYPE_ENTER_PATTERN_STRING = "<type((?s).+?)>";

	/** String pattern that closes a type in a TAF XML file. */
	private static final String TYPE_EXIT_PATTERN_STRING = "</type>";

	/**
	 * String pattern to get a parameter or a constraint with its parameters in a
	 * TAF XML file.
	 */
	private static final String PARAMETER_CONSTRAINT_PATTERN_STRING = "<(parameter|constraint)((?s).+?)/>";

	/** String pattern to retrieve all whitespace characters. */
	private static final String MULTIPLE_WHITESPACE_CHARS_PATTERN_STRING = "((?s)[\\s]+)";

	/** Entity representation in the save file. */
	// TODO Add to Save manager?
	private static final String SAVE_LINE_FORMAT = "entity=\"%s\" parent=\"%d\" %s\n";

	/** The pattern object of {@link #ROOT_PATTERN_STRING}. */
	private static final Pattern ROOT_PATTERN = Pattern.compile(ROOT_PATTERN_STRING);

	/** The pattern object of {@link #NODE_ENTER_PATTERN_STRING}. */
	private static final Pattern NODE_ENTER_PATTERN = Pattern.compile(NODE_ENTER_PATTERN_STRING);

	/** The pattern object of {@link #NODE_EXIT_PATTERN_STRING}. */
	private static final Pattern NODE_EXIT_PATTERN = Pattern.compile(NODE_EXIT_PATTERN_STRING);

	/** The pattern object of {@link #TYPE_ENTER_PATTERN_STRING}. */
	private static final Pattern TYPE_ENTER_PATTERN = Pattern.compile(TYPE_ENTER_PATTERN_STRING);

	/** The pattern object of {@link #TYPE_EXIT_PATTERN_STRING}. */
	private static final Pattern TYPE_EXIT_PATTERN = Pattern.compile(TYPE_EXIT_PATTERN_STRING);

	/** The pattern object of {@link #PARAMETER_CONSTRAINT_PATTERN_STRING}. */
	private static final Pattern PARAMETER_CONSTRAINT_PATTERN = Pattern.compile(PARAMETER_CONSTRAINT_PATTERN_STRING);

	/** The file to read. */
	private File tafFile;

	/**
	 * Instantiates a new XML taf reader on a file.
	 *
	 * @param tafFile the taf file
	 */
	public XMLTafReader(File tafFile) {
		this.tafFile = tafFile;
	}

	/**
	 * Reads the XML file and returns the string representation of the save file.
	 *
	 * @return the extracted save file
	 * @throws ImportException if an error is thrown
	 */
	public String readFile() throws ImportException {
		String convertedLines = "";

		// Fetch all lines
		String fileLines = "";
		try (BufferedReader reader = new BufferedReader(new FileReader(tafFile))) {
			String line;
			while ((line = reader.readLine()) != null) {
				fileLines += line + "\n";
			}
		} catch (IOException e) {
			throw new ImportException(XMLTafReader.class, FILE_READ_ERROR_MESSAGE + e.getMessage());
		}

		// Get the root content
		Matcher m = ROOT_PATTERN.matcher(fileLines);
		if (!m.find()) {
			throw new ImportException(XMLTafReader.class, NO_ROOT_ERROR_MESSAGE);
		}

		String rootName = m.group(1);
		String rootContent = m.group(2);
		convertedLines += SAVE_LINE_FORMAT.formatted(Consts.NODE_ENTITY_NAME, -1, rootName.strip());

		RangeTree tree = registerTypesAndNodes(rootContent);
		// Register types and nodes (position, [parameters, isNode])
		Map<Integer, Pair<String, Boolean>> typeNodesPositionMap = new LinkedHashMap<Integer, Pair<String, Boolean>>();
		m = TYPE_ENTER_PATTERN.matcher(rootContent);
		while (m.find()) {
			int position = m.start();
			String parameters = m.group(1).replaceAll(MULTIPLE_WHITESPACE_CHARS_PATTERN_STRING, " ");
			typeNodesPositionMap.put(position, new Pair<String, Boolean>(parameters, false));
		}

		m = NODE_ENTER_PATTERN.matcher(rootContent);
		while (m.find()) {
			int position = m.start();
			String parameters = m.group(1).replaceAll(MULTIPLE_WHITESPACE_CHARS_PATTERN_STRING, " ");
			typeNodesPositionMap.put(position, new Pair<String, Boolean>(parameters, true));
		}
		for (Map.Entry<Integer, Pair<String, Boolean>> entry : typeNodesPositionMap.entrySet()) {
			Integer position = entry.getKey();
			Pair<String, Boolean> info = entry.getValue();
			String parameters = info.getKey();
			boolean isNode = info.getValue();

			int nodeId = tree.getRangeId(position);
			String entityName = isNode ? Consts.NODE_ENTITY_NAME : Consts.TYPE_ENTITY_NAME;
			convertedLines += SAVE_LINE_FORMAT.formatted(entityName, tree.getParentId(nodeId), parameters.strip());
		}

		m = PARAMETER_CONSTRAINT_PATTERN.matcher(fileLines);
		while (m.find()) {
			int position = m.start();
			int groupCount = m.groupCount();
			String entity = m.group(1);
			String parameters = "";
			if (groupCount > 1) {
				parameters = m.group(2);
				parameters = parameters.replaceAll(MULTIPLE_WHITESPACE_CHARS_PATTERN_STRING, " ");
			}
			convertedLines += SAVE_LINE_FORMAT.formatted(entity, tree.getRangeId(position), parameters.strip());
		}

		return convertedLines;
	}

	/**
	 * <p>
	 * Register elements in a range tree using their enter and exit positions.
	 * </p>
	 * 
	 * <p>
	 * The algorithm is simple: for every exit positions (in FIFO order) we search
	 * for the first enter position that is greater (say at index <code>i</code>).
	 * If it exist, it means that the enter position corresponding to the exit
	 * position is at index <code>i - 1</code>. If it does not exist, it means that
	 * the corresponding enter position is the last element of the list. We then
	 * remove both enter and exit positions from their list. If the first enter
	 * position is greater than the exit position, then there is an error since it
	 * would mean that we exit the element before even entering it.
	 * </p>
	 *
	 * @param tree          the tree with elements ranges
	 * @param enterPosition the enter position of the elements
	 * @param exitPosition  the exit position of the elements
	 * @throws ImportException the import exception if the lists are not of the same
	 *                         size, or if an enter position is after an exit
	 *                         position.
	 */
	private void registerElements(RangeTree tree, List<Integer> enterPosition, Queue<Integer> exitPosition)
			throws ImportException {
		// Check size
		if (enterPosition.size() != exitPosition.size()) {
			throw new ImportException(XMLTafReader.class, NODES_ENTER_EXIT_NUMBER_MISSMATCH_ERROR_MESSAGE);
		}

		while (!exitPosition.isEmpty()) {
			int end = exitPosition.poll();
			if (enterPosition.get(0) >= end) {
				throw new ImportException(XMLTafReader.class, NODES_EXIT_AFTER_ENTER_ERROR_MESSAGE);
			}

			int index = enterPosition.size() - 1;
			for (int i = 1; i < enterPosition.size(); i++) {
				int start = enterPosition.get(i);
				if (start > end) {
					// The previous element is the start position
					index = i - 1;
					break;
				}
			}

			int start = enterPosition.get(index);
			enterPosition.remove(index);
			try {
				tree.addRange(start, end);
			} catch (RangeException e) {
				throw new ImportException(XMLTafReader.class, RANGE_ERROR_MESSAGE + e.getMessage());
			}
		}
	}

	/**
	 * Register types and nodes in the range tree, and number the tree elements in
	 * the depth-first format.
	 *
	 * @param rootContent the TAF content in the root
	 * @return a filled and numbered range tree
	 * @throws ImportException if {@link #registerElements(RangeTree, List, Queue)}
	 *                         failed.
	 */
	private RangeTree registerTypesAndNodes(String rootContent) throws ImportException {
		List<Integer> typeEnterPosition = new ArrayList<Integer>();
		Queue<Integer> typeExitPosition = new LinkedList<Integer>();
		List<Integer> nodeEnterPosition = new ArrayList<Integer>();
		Queue<Integer> nodeExitPosition = new LinkedList<Integer>();

		// Enter types
		Matcher m = TYPE_ENTER_PATTERN.matcher(rootContent);
		m.results().forEach(t -> typeEnterPosition.add(t.start()));

		// Enter nodes
		m = NODE_ENTER_PATTERN.matcher(rootContent);
		m.results().forEach(t -> nodeEnterPosition.add(t.start()));

		// Exit types
		m = TYPE_EXIT_PATTERN.matcher(rootContent);
		m.results().forEach(t -> typeExitPosition.add(t.start()));

		// Exit nodes
		m = NODE_EXIT_PATTERN.matcher(rootContent);
		m.results().forEach(t -> nodeExitPosition.add(t.start()));

		// Create range tree
		RangeTree tree = new RangeTree();

		// Register types
		registerElements(tree, typeEnterPosition, typeExitPosition);

		// Register nodes
		registerElements(tree, nodeEnterPosition, nodeExitPosition);

		tree.numberTree();
		return tree;
	}
}
