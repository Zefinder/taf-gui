package com.taf.frame.panel.run;

import static com.taf.manager.RunManager.EXPERIMENT_FOLDER_NAME_STRING;
import static com.taf.manager.RunManager.EXPERIMENT_PATH_STRING;
import static com.taf.manager.RunManager.MAX_BACKTRACKING_STRING;
import static com.taf.manager.RunManager.MAX_DIVERSITY_STRING;
import static com.taf.manager.RunManager.NB_TEST_ARTIFACTS_STRING;
import static com.taf.manager.RunManager.NB_TEST_CASES_STRING;
import static com.taf.manager.RunManager.NODE_MAX_NB_INSTANCES_STRING;
import static com.taf.manager.RunManager.PARAMETER_MAX_NB_INSTANCES_STRING;
import static com.taf.manager.RunManager.STRING_PARAMETER_MAX_SIZE_STRING;
import static com.taf.manager.RunManager.TEMPLATE_FILE_NAME_STRING;
import static com.taf.manager.RunManager.TEMPLATE_PATH_STRING;
import static com.taf.manager.RunManager.TEST_ARTIFACT_FOLDER_NAME_STRING;
import static com.taf.manager.RunManager.TEST_CASE_FOLDER_NAME_STRING;
import static com.taf.manager.RunManager.Z3_TIMEOUT_STRING;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.taf.manager.ConstantManager;
import com.taf.manager.RunManager;

public class SettingsPanel extends JPanel {

	private static final long serialVersionUID = 2148563875281479934L;

	private static final String DELETE_EXPERIMENT_FOLDER_CHECKBOX_TEXT = "Delete experiment folder name before running TAF";

	private static final String RUN_BUTTON_TEXT = "Run";

	public SettingsPanel() {
		this.setLayout(new GridBagLayout());
		RunManager runManager = RunManager.getInstance();

		// TODO Fill values with the settings.xml if exists
		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.gridy = 0;
		JTextField templatePathField = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		templatePathField.setText(runManager.getTemplatePath());
		addLabeledField(TEMPLATE_PATH_STRING, templatePathField, c);

		JTextField templateFileNameField = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		templateFileNameField.setText(runManager.getTemplateFileName());
		addLabeledField(TEMPLATE_FILE_NAME_STRING, templateFileNameField, c);

		JTextField experimentPathField = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		experimentPathField.setText(runManager.getExperimentPath());
		addLabeledField(EXPERIMENT_PATH_STRING, experimentPathField, c);

		JTextField experimentFolderNameField = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		experimentFolderNameField.setText(runManager.getExperimentFolderName());
		addLabeledField(EXPERIMENT_FOLDER_NAME_STRING, experimentFolderNameField, c);

		JFormattedTextField nbTestCasesField = new JFormattedTextField(runManager.getNbTestCases());
		nbTestCasesField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
//		nbTestCasesField.addPropertyChangeListener(ConstantManager.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
//				evt -> nbTestCases = ((Number) nbTestCasesField.getValue()).intValue());
		addLabeledField(NB_TEST_CASES_STRING, nbTestCasesField, c);

		JTextField testCaseFolderNameField = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		testCaseFolderNameField.setText(runManager.getTestCaseFolderName());
		addLabeledField(TEST_CASE_FOLDER_NAME_STRING, testCaseFolderNameField, c);

		JFormattedTextField nbTestArtifactsField = new JFormattedTextField(runManager.getNbTestArtifacts());
		nbTestArtifactsField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
//		nbTestArtifactsField.addPropertyChangeListener(ConstantManager.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
//				evt -> nbTestArtifacts = ((Number) nbTestArtifactsField.getValue()).intValue());
		addLabeledField(NB_TEST_ARTIFACTS_STRING, nbTestArtifactsField, c);

		JTextField testArtifactFolderNameField = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		testArtifactFolderNameField.setText(runManager.getTestArtifactFolderName());
		addLabeledField(TEST_ARTIFACT_FOLDER_NAME_STRING, testArtifactFolderNameField, c);

		JFormattedTextField parameterMaxNbInstancesField = new JFormattedTextField(
				runManager.getParameterMaxNbInstances());
		parameterMaxNbInstancesField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
//		parameterMaxNbInstancesField.addPropertyChangeListener(ConstantManager.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
//				evt -> parameterMaxNbInstances = ((Number) parameterMaxNbInstancesField.getValue()).intValue());
		addLabeledField(PARAMETER_MAX_NB_INSTANCES_STRING, parameterMaxNbInstancesField, c);

		JFormattedTextField stringParameterMaxSizeField = new JFormattedTextField(
				runManager.getStringParameterMaxSize());
		stringParameterMaxSizeField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
//		stringParameterMaxSizeField.addPropertyChangeListener(ConstantManager.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
//				evt -> stringParameterMaxSize = ((Number) stringParameterMaxSizeField.getValue()).intValue());
		addLabeledField(STRING_PARAMETER_MAX_SIZE_STRING, stringParameterMaxSizeField, c);

		JFormattedTextField nodeMaxNbInstancesField = new JFormattedTextField(runManager.getNodeMaxNbInstances());
		nodeMaxNbInstancesField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
//		nodeMaxNbInstancesField.addPropertyChangeListener(ConstantManager.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
//				evt -> nodeMaxNbInstances = ((Number) nodeMaxNbInstancesField.getValue()).intValue());
		addLabeledField(NODE_MAX_NB_INSTANCES_STRING, nodeMaxNbInstancesField, c);

		JFormattedTextField maxBacktrackingField = new JFormattedTextField(runManager.getMaxBacktracking());
		maxBacktrackingField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
//		maxBacktrackingField.addPropertyChangeListener(ConstantManager.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
//				evt -> maxBacktracking = ((Number) maxBacktrackingField.getValue()).intValue());
		addLabeledField(MAX_BACKTRACKING_STRING, maxBacktrackingField, c);

		JFormattedTextField maxDiversityField = new JFormattedTextField(runManager.getMaxDiversity());
		maxDiversityField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
//		maxDiversityField.addPropertyChangeListener(ConstantManager.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
//				evt -> maxDiversity = ((Number) maxDiversityField.getValue()).intValue());
		addLabeledField(MAX_DIVERSITY_STRING, maxDiversityField, c);

		JFormattedTextField z3TimeoutField = new JFormattedTextField(runManager.getZ3Timeout());
		z3TimeoutField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
//		z3TimeoutField.addPropertyChangeListener(ConstantManager.JFORMATTED_TEXT_FIELD_VALUE_PROPERTY,
//				evt -> z3Timeout = ((Number) z3TimeoutField.getValue()).intValue());
		addLabeledField(Z3_TIMEOUT_STRING, z3TimeoutField, c);

		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(ConstantManager.LARGE_INSET_GAP, 0, 0, 0);
		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		JButton runButton = new JButton(RUN_BUTTON_TEXT);
		runButton.addActionListener(e -> {
			runManager.setTemplatePath(templatePathField.getText());
			runManager.setTemplateFileName(templateFileNameField.getText());
			runManager.setExperimentPath(experimentPathField.getText());
			runManager.setExperimentFolderName(experimentFolderNameField.getText());
			runManager.setNbTestCases(((Number) nbTestCasesField.getValue()).intValue());
			runManager.setTestCaseFolderName(testCaseFolderNameField.getText());
			runManager.setNbTestArtifacts(((Number) nbTestArtifactsField.getValue()).intValue());
			runManager.setTestArtifactFolderName(testArtifactFolderNameField.getText());
			runManager.setParameterMaxNbInstances(((Number) parameterMaxNbInstancesField.getValue()).intValue());
			runManager.setStringParameterMaxSize(((Number) stringParameterMaxSizeField.getValue()).intValue());
			runManager.setNodeMaxNbInstances(((Number) nodeMaxNbInstancesField.getValue()).intValue());
			runManager.setMaxBacktracking(((Number) maxBacktrackingField.getValue()).intValue());
			runManager.setMaxDiversity(((Number) maxDiversityField.getValue()).intValue());
			runManager.setZ3Timeout(((Number) z3TimeoutField.getValue()).intValue());

			try {
				runManager.run();
			} catch (IOException e1) {
				ConstantManager.showError("Something wrong happened when running TAF: " + e1.getMessage());
				e1.printStackTrace();
			}
		});
		this.add(runButton, c);
	}

	// TODO Add to constant manager and use on a lot of panels
	private void addLabeledField(String text, JTextField field, GridBagConstraints c) {
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(ConstantManager.SMALL_INSET_GAP, 0, 0, ConstantManager.SMALL_INSET_GAP);
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		JLabel label = new JLabel(text);
		this.add(label, c);

		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		this.add(field, c);
		c.gridy++;
	}

}
