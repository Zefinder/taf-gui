package com.taf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taf.exception.ImportException;
import com.taf.exception.RangeIntersectionException;
import com.taf.manager.ConstantManager;

public class XMLTafReader {

	private static final String FILE_READ_ERROR_MESSAGE = "Something happened when trying to read the file to import: ";
	private static final String NO_ROOT_ERROR_MESSAGE = "There is no root in the imported project!";
	private static final String NODES_ENTER_EXIT_NUMBER_MISSMATCH_ERROR_MESSAGE = "The number of <node> and </node> is different!";
	private static final String NODES_EXIT_AFTER_ENTER_ERROR_MESSAGE = "Element </node> is before <node>!";
	private static final String RANGE_ERROR_MESSAGE = "Error when adding the position range: ";

	private static final String ROOT_PATTERN_STRING = "<root((?s).+?)>((?s).+?)</root>";
	private static final String NODE_ENTER_PATTERN_STRING = "<node((?s).+?)>";
	private static final String NODE_EXIT_PATTERN_STRING = "</node>";
	private static final String PARAMETER_CONSTRAINT_PATTERN_STRING = "<(parameter|constraint)((?s).+?)/>";
	private static final String MULTIPLE_WHITESPACE_CHARS_PATTERN_STRING = "((?s)[\\s]+)";

	// TODO Add to Save manager?
	private static final String SAVE_LINE_FORMAT = "entity=\"%s\" parent=\"%d\" %s\n";

	private static final Pattern ROOT_PATTERN = Pattern.compile(ROOT_PATTERN_STRING);
	private static final Pattern NODE_ENTER_PATTERN = Pattern.compile(NODE_ENTER_PATTERN_STRING);
	private static final Pattern NODE_EXIT_PATTERN = Pattern.compile(NODE_EXIT_PATTERN_STRING);
	private static final Pattern PARAMETER_CONSTRAINT_PATTERN = Pattern.compile(PARAMETER_CONSTRAINT_PATTERN_STRING);

	private File tafFile;

	public XMLTafReader(File tafFile) {
		this.tafFile = tafFile;
	}

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
			throw new ImportException(FILE_READ_ERROR_MESSAGE + e.getMessage());
		}

		// Get the root content
		Matcher m = ROOT_PATTERN.matcher(fileLines);
		if (!m.find()) {
			throw new ImportException(NO_ROOT_ERROR_MESSAGE);
		}

		String rootName = m.group(1);
		String rootContent = m.group(2);
		convertedLines += SAVE_LINE_FORMAT.formatted(ConstantManager.NODE_ENTITY_NAME, -1, rootName.strip());

		// Register nodes
		RangeTree tree = registerNodes(rootContent);
		m = NODE_ENTER_PATTERN.matcher(rootContent);
		while (m.find()) {
			int position = m.start();
			int nodeId = tree.getNodeId(position);
			String parameters = m.group(1).replaceAll(MULTIPLE_WHITESPACE_CHARS_PATTERN_STRING, " ");
			convertedLines += SAVE_LINE_FORMAT.formatted(ConstantManager.NODE_ENTITY_NAME, tree.getParentId(nodeId),
					parameters.strip());
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
			convertedLines += SAVE_LINE_FORMAT.formatted(entity, tree.getNodeId(position), parameters.strip());
		}
		
		return convertedLines;
	}

	private RangeTree registerNodes(String rootContent) throws ImportException {
		List<Integer> nodeEnterPosition = new ArrayList<Integer>();
		Queue<Integer> nodeExitPosition = new LinkedList<Integer>();

		// Enter nodes
		Matcher m = NODE_ENTER_PATTERN.matcher(rootContent);
		m.results().forEach(t -> nodeEnterPosition.add(t.start()));

		// Exit nodes
		m = NODE_EXIT_PATTERN.matcher(rootContent);
		m.results().forEach(t -> nodeExitPosition.add(t.start()));

		// Check size
		if (nodeEnterPosition.size() != nodeExitPosition.size()) {
			throw new ImportException(NODES_ENTER_EXIT_NUMBER_MISSMATCH_ERROR_MESSAGE);
		}

		// Create range tree
		RangeTree tree = new RangeTree();
		while (!nodeExitPosition.isEmpty()) {
			int end = nodeExitPosition.poll();
			if (nodeEnterPosition.get(0) >= end) {
				throw new ImportException(NODES_EXIT_AFTER_ENTER_ERROR_MESSAGE);
			}

			int index = nodeEnterPosition.size() - 1;
			for (int i = 1; i < nodeEnterPosition.size(); i++) {
				int start = nodeEnterPosition.get(i);
				if (start > end) {
					// The previous element is the start position
					index = i - 1;
				}
			}

			int start = nodeEnterPosition.get(index);
			nodeEnterPosition.remove(index);
			try {
				tree.addRange(start, end);
			} catch (RangeIntersectionException e) {
				throw new ImportException(RANGE_ERROR_MESSAGE + e.getMessage());
			}
		}

		tree.numberTree();
		return tree;
	}
}
