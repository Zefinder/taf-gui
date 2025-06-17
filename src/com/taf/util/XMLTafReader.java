package com.taf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taf.exception.ImportException;

public class XMLTafReader {
	
	private static final String FILE_READ_ERROR_MESSAGE = "Something happened when trying to read the file to import: ";
	private static final String NO_ROOT_ERROR_MESSAGE = "There is no root in the imported project!";
	private static final String NODES_ENTER_EXIT_ERROR_MESSAGE = "The number of <node> and </node> is different!";

	private static final String ROOT_PATTERN_STRING = "<root((?s).+?)>((?s).+?)</root>";
	private static final String NODE_ENTER_PATTERN_STRING = "<node((?s).+?)>";
	private static final String NODE_EXIT_PATTERN_STRING = "</node>";
	private static final String PARAMETER_CONSTRAINT_PATTERN_STRING = "<(parameter|constraint)(?:((?s).+?))/>";
	private static final String MULTIPLE_WHITESPACE_CHARS_PATTERN_STRING = "((?s)[\\s]+)";

	private static final Pattern ROOT_PATTERN = Pattern.compile(ROOT_PATTERN_STRING);
	private static final Pattern NODE_ENTER_PATTERN = Pattern.compile(NODE_ENTER_PATTERN_STRING);
	private static final Pattern NODE_EXIT_PATTERN = Pattern.compile(NODE_EXIT_PATTERN_STRING);
	private static final Pattern PARAMETER_CONSTRAINT_PATTERN = Pattern.compile(PARAMETER_CONSTRAINT_PATTERN_STRING);

	private File tafFile;

	public XMLTafReader(File tafFile) {
		this.tafFile = tafFile;
	}

	public void readFile() throws ImportException {
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
		
		registerNodes(rootContent);
//		Matcher m = PARAMETER_CONSTRAINT_PATTERN.matcher(fileLines);
//		m.results().forEach(t -> System.out.println(t.group(1)));
//		while (m.find()) {
//			int groupCount = m.groupCount();
//			String entity = m.group(1);
//			String parameters = "";
//			if (groupCount > 1) {
//				parameters = m.group(2);
//				parameters = parameters.replaceAll(MULTIPLE_WHITESPACE_CHARS_PATTERN_STRING, " ");
//			}
//			System.out.println("entity=%s %s".formatted(entity, parameters.strip()));
//		}
	}
	
	private boolean registerNodes(String rootContent) throws ImportException {
		List<Integer> nodeEnterPosition = new ArrayList<Integer>();
		List<Integer> nodeExitPosition = new ArrayList<Integer>();
		
		// Enter nodes
		Matcher m = NODE_ENTER_PATTERN.matcher(rootContent);
		m.results().forEach(t -> nodeEnterPosition.add(t.start()));
		System.out.println(nodeEnterPosition.toString());
		
		// Exit nodes
		m = NODE_EXIT_PATTERN.matcher(rootContent);
		m.results().forEach(t -> nodeExitPosition.add(t.start()));
		System.out.println(nodeExitPosition.toString());
		
		// Check size
		if (nodeEnterPosition.size() != nodeExitPosition.size()) {
			throw new ImportException(NODES_ENTER_EXIT_ERROR_MESSAGE);
		}
		
		Queue<Integer> a = new LinkedList<Integer>(); 
		
		return true;
	}

	public static void main(String[] args) throws ImportException {
		var a = new XMLTafReader(
				new File("/home/jakub/eclipse-workspace/test/src/main/java/test/simbav1/trajectory_template.xml"));
//		a.readFile();
		
		a.registerNodes("""
				<node a>
					<node a>
					</node>
				</node>
				<node a>
				</node>
				""");
	}

}
