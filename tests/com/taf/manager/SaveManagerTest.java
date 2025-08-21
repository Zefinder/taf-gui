package com.taf.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.taf.exception.ImportException;
import com.taf.exception.ParseException;
import com.taf.logic.field.RootTest;
import com.taf.util.Consts;

class SaveManagerTest extends ManagerTest {

	private static final String COMPLETE_ROOT_SAVE = """
			entity="node" parent="-1" name="test"
			entity="type" parent="0" name="empty_type"
			entity="type" parent="0" name="filled_type"
			entity="node" parent="2" name="empty_type_node" nb_instances="1"
			entity="node" parent="0" name="filled_node" min="1" max="1"
			entity="node" parent="0" name="typed_node" type="empty_type" nb_instances="1" depth="1"
			entity="node" parent="5" name="ref_node" ref="filled_node" nb_instances="1" min_depth="1" max_depth="1"
			entity="constraint" parent="2" name="empty_type_constraint"
			entity="parameter" parent="4" name="boolean_parameter" type="boolean" values="False;True" weights="1;1"
			entity="parameter" parent="4" name="integer_parameter" type="integer" min="0" max="10" distribution="u"
			entity="parameter" parent="4" name="integer_normal_parameter" type="integer" min="0" max="10" distribution="n" mean="0" variance="0"
			entity="parameter" parent="4" name="integer_interval_parameter" type="integer" min="0" max="10" distribution="i" ranges="[0, 10]" weights="1"
			entity="parameter" parent="4" name="real_parameter" type="real" min="0" max="10" distribution="u"
			entity="parameter" parent="4" name="real_normal_parameter" type="real" min="0" max="10" distribution="n" mean="0" variance="0"
			entity="parameter" parent="4" name="real_interval_parameter" type="real" min="0" max="10" distribution="i" ranges="[0, 10]" weights="1"
			entity="parameter" parent="4" name="string_parameter" type="string" values="a;b" weights="1;2"
			entity="constraint" parent="4" name="filled_constraint" expressions="i + j < 10" quantifiers="i;j" ranges="[0, 10];[0, filled_node.nb_instances]" types="forall;exists"
			""";
	
	// TODO Add test create projects with same name
	// TODO Add test import already existing project

//	@Test
	void testSaveManagerImportSaveExport() throws IOException {
		// Write the XML in a file and import it
		File tmpFile = File.createTempFile("tmp", "savemanagertest" + Consts.XML_FILE_EXTENSION);
		BufferedWriter writer = new BufferedWriter(new FileWriter(tmpFile));
		writer.write(RootTest.COMPLETE_ROOT_XML);
		writer.close();

		try {
			SaveManager.getInstance().importProject(tmpFile);
		} catch (ImportException e) {
			fail("An import error occured when it should not have: " + e.getMessage());
		}

		// Check the save file
		String tmpTafFileName = tmpFile.getName().replace(Consts.XML_FILE_EXTENSION, Consts.TAF_FILE_EXTENSION);
		File saveFile = new File(mainDirectory + File.separator + tmpTafFileName);
		String saveFileLines = "";
		BufferedReader reader = new BufferedReader(new FileReader(saveFile));
		String line = "";
		while ((line = reader.readLine()) != null) {
			saveFileLines += line + "\n";
		}
		reader.close();
		assertEquals(COMPLETE_ROOT_SAVE, saveFileLines);

		// Open it and export to XML
		try {
			SaveManager.getInstance().openProject(tmpTafFileName);
		} catch (IOException | ParseException e) {
			fail("A parse error occured when it should not have: " + e.getMessage());
		}
		File xmlFile = new File(mainDirectory + File.separator + tmpFile.getName());
		SaveManager.getInstance().exportToXML(xmlFile);

		// Check the XML file
		String xmlFileLines = "";
		reader = new BufferedReader(new FileReader(xmlFile));
		line = "";
		while ((line = reader.readLine()) != null) {
			xmlFileLines += line + "\n";
		}
		reader.close();
		String expected = """
				<?xml version="1.0"?>


				""" + RootTest.COMPLETE_ROOT_XML;
		assertEquals(expected, xmlFileLines.stripTrailing());

		// Delete the temporary file
		tmpFile.delete();
	}

}
