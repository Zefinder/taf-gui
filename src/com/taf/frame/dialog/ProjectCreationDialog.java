package com.taf.frame.dialog;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.taf.manager.ConstantManager;

public class ProjectCreationDialog extends InputInformationDialog {

	private static final long serialVersionUID = -8365643430677886812L;
	
	private static final String DIALOG_TITLE = "Create a new project";
	
	private static final String PROJECT_LABEL_TEXT = "Project name";

	private final JTextField projectField;

	private String projectName;

	public ProjectCreationDialog() {
		this.setTitle(DIALOG_TITLE);

		// TODO Replace with ConstantManager
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(ConstantManager.HUGE_INSET_GAP, ConstantManager.LARGE_INSET_GAP, ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel projectLabel = new JLabel(PROJECT_LABEL_TEXT);
		addComponent(projectLabel, c);

		c.insets = new Insets(ConstantManager.HUGE_INSET_GAP, ConstantManager.SMALL_INSET_GAP, ConstantManager.SMALL_INSET_GAP, ConstantManager.LARGE_INSET_GAP);
		c.gridx = 1;
		projectField = new JTextField(ConstantManager.JTEXT_FIELD_DEFAULT_COLUMN);
		addComponent(projectField, c);
	}

	@Override
	protected void performAction() {
		String name = projectField.getText();
		if (!name.isBlank()) {
			// Check if ends with .taf, adds it otherwise
			if (!name.endsWith(ConstantManager.TAF_FILE_EXTENSION)) {
				name += ConstantManager.TAF_FILE_EXTENSION;
			}
			
			projectName = name;
			dispose();
		}
	}

	public String getProjectName() {
		return projectName;
	}

	@Override
	public void initDialog() {
		projectName = null;
		super.initDialog();
	}

}
