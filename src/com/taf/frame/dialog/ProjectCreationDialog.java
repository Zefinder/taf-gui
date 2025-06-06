package com.taf.frame.dialog;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class ProjectCreationDialog extends InputInformationDialog {

	private static final long serialVersionUID = -8365643430677886812L;

	private final JTextField fieldName;

	private String projectName;

	public ProjectCreationDialog() {
		this.setTitle("Create a new project");

		// TODO Replace with ConstantManager
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(20, 15, 5, 5);
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		JLabel fieldLabel = new JLabel("Project name");
		addComponent(fieldLabel, c);

		c.insets = new Insets(20, 5, 5, 15);
		c.gridx = 1;
		fieldName = new JTextField(20);
		addComponent(fieldName, c);
	}

	@Override
	protected void performAction() {
		String name = fieldName.getText();
		if (!name.isBlank()) {
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
