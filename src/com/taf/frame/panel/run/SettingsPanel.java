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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.taf.event.EventListener;
import com.taf.event.EventMethod;
import com.taf.event.ProjectRunAbortedEvent;
import com.taf.event.ProjectRunStartedEvent;
import com.taf.event.ProjectRunStoppedEvent;
import com.taf.event.RunLocationChangedEvent;
import com.taf.manager.ConstantManager;
import com.taf.manager.EventManager;
import com.taf.manager.RunManager;

public class SettingsPanel extends JPanel implements EventListener {

	private static final long serialVersionUID = 2148563875281479934L;

	private static final String DELETE_EXPERIMENT_FOLDER_CHECKBOX_TEXT = "Delete experiment folder name before running TAF";

	private static final String RUN_BUTTON_TEXT = "Run";
	private static final String STOP_BUTTON_TEXT = "Stop";

	private JTextField templatePathField;
	private JTextField templateFileNameField;
	private JTextField experimentPathField;
	private JTextField experimentFolderNameField;
	private JFormattedTextField nbTestCasesField;
	private JTextField testCaseFolderNameField;
	private JFormattedTextField nbTestArtifactsField;
	private JTextField testArtifactFolderNameField;
	private JFormattedTextField parameterMaxNbInstancesField;
	private JFormattedTextField stringParameterMaxSizeField;
	private JFormattedTextField nodeMaxNbInstancesField;
	private JFormattedTextField maxBacktrackingField;
	private JFormattedTextField maxDiversityField;
	private JFormattedTextField z3TimeoutField;

	private JButton runButton;
	private JButton stopButton;

	public SettingsPanel() {
		this.setLayout(new GridBagLayout());
		RunManager runManager = RunManager.getInstance();

		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.gridy = 0;
		templatePathField = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addLabeledField(TEMPLATE_PATH_STRING, templatePathField, c);

		templateFileNameField = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addLabeledField(TEMPLATE_FILE_NAME_STRING, templateFileNameField, c);

		experimentPathField = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addLabeledField(EXPERIMENT_PATH_STRING, experimentPathField, c);

		experimentFolderNameField = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addLabeledField(EXPERIMENT_FOLDER_NAME_STRING, experimentFolderNameField, c);

		nbTestCasesField = new JFormattedTextField(runManager.getNbTestCases());
		nbTestCasesField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addLabeledField(NB_TEST_CASES_STRING, nbTestCasesField, c);

		testCaseFolderNameField = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addLabeledField(TEST_CASE_FOLDER_NAME_STRING, testCaseFolderNameField, c);

		nbTestArtifactsField = new JFormattedTextField(runManager.getNbTestArtifacts());
		nbTestArtifactsField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addLabeledField(NB_TEST_ARTIFACTS_STRING, nbTestArtifactsField, c);

		testArtifactFolderNameField = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addLabeledField(TEST_ARTIFACT_FOLDER_NAME_STRING, testArtifactFolderNameField, c);

		parameterMaxNbInstancesField = new JFormattedTextField(runManager.getParameterMaxNbInstances());
		parameterMaxNbInstancesField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addLabeledField(PARAMETER_MAX_NB_INSTANCES_STRING, parameterMaxNbInstancesField, c);

		stringParameterMaxSizeField = new JFormattedTextField(runManager.getStringParameterMaxSize());
		stringParameterMaxSizeField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addLabeledField(STRING_PARAMETER_MAX_SIZE_STRING, stringParameterMaxSizeField, c);

		nodeMaxNbInstancesField = new JFormattedTextField(runManager.getNodeMaxNbInstances());
		nodeMaxNbInstancesField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addLabeledField(NODE_MAX_NB_INSTANCES_STRING, nodeMaxNbInstancesField, c);

		maxBacktrackingField = new JFormattedTextField(runManager.getMaxBacktracking());
		maxBacktrackingField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addLabeledField(MAX_BACKTRACKING_STRING, maxBacktrackingField, c);

		maxDiversityField = new JFormattedTextField(runManager.getMaxDiversity());
		maxDiversityField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addLabeledField(MAX_DIVERSITY_STRING, maxDiversityField, c);

		z3TimeoutField = new JFormattedTextField(runManager.getZ3Timeout());
		z3TimeoutField.setColumns(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addLabeledField(Z3_TIMEOUT_STRING, z3TimeoutField, c);
		
		updateFields();

		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(ConstantManager.SMALL_INSET_GAP, 0, 0, 0);
		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		JCheckBox deleteExperimentBox = new JCheckBox(DELETE_EXPERIMENT_FOLDER_CHECKBOX_TEXT);
		deleteExperimentBox.setSelected(false);
		this.add(deleteExperimentBox, c);

		c.insets = new Insets(ConstantManager.MEDIUM_INSET_GAP, 0, ConstantManager.MEDIUM_INSET_GAP, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridy++;
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		runButton = new JButton(RUN_BUTTON_TEXT);
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
			runManager.setDeleteExperimentFolder(deleteExperimentBox.isSelected());

			try {
				runManager.run();
			} catch (IOException e1) {
				ConstantManager.showError("Something wrong happened when running TAF: " + e1.getMessage());
				e1.printStackTrace();
			}
		});
		buttonPanel.add(runButton);
		buttonPanel.add(Box.createHorizontalStrut(10));

		stopButton = new JButton(STOP_BUTTON_TEXT);
		stopButton.addActionListener(e -> EventManager.getInstance().fireEvent(new ProjectRunAbortedEvent()));
		stopButton.setEnabled(false);
		buttonPanel.add(stopButton);
		this.add(buttonPanel, c);

		EventManager.getInstance().registerEventListener(this);
	}

	private void updateFields() {
		RunManager runManager = RunManager.getInstance();
		templatePathField.setText(runManager.getTemplatePath());
		templateFileNameField.setText(runManager.getTemplateFileName());
		experimentPathField.setText(runManager.getExperimentPath());
		experimentFolderNameField.setText(runManager.getExperimentFolderName());
		nbTestCasesField.setValue(runManager.getNbTestCases());
		testCaseFolderNameField.setText(runManager.getTestCaseFolderName());
		nbTestArtifactsField.setValue(runManager.getNbTestArtifacts());
		testArtifactFolderNameField.setText(runManager.getTestArtifactFolderName());
		parameterMaxNbInstancesField.setValue(runManager.getParameterMaxNbInstances());
		stringParameterMaxSizeField.setValue(runManager.getStringParameterMaxSize());
		nodeMaxNbInstancesField.setValue(runManager.getNodeMaxNbInstances());
		maxBacktrackingField.setValue(runManager.getMaxBacktracking());
		maxDiversityField.setValue(runManager.getMaxDiversity());
		z3TimeoutField.setValue(runManager.getZ3Timeout());
	}

	// TODO Add to constant manager and use on a lot of panels
	private void addLabeledField(String text, JTextField field, GridBagConstraints c) {
		// Add label
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(ConstantManager.SMALL_INSET_GAP, 0, 0, ConstantManager.SMALL_INSET_GAP);
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		JLabel label = new JLabel(text);
		this.add(label, c);

		// Add field
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		this.add(field, c);

		c.gridy++;
	}

	@EventMethod
	public void onRunStartedEvent(ProjectRunStartedEvent event) {
		runButton.setEnabled(false);
		stopButton.setEnabled(true);
	}

	@EventMethod
	public void onRunStoppedEvent(ProjectRunStoppedEvent event) {
		runButton.setEnabled(true);
		stopButton.setEnabled(false);
	}

	@EventMethod
	public void onLocationChanged(RunLocationChangedEvent event) {
		updateFields();
	}
	
	@Override
	public void unregisterComponents() {
		// Nothing here
	}

}
