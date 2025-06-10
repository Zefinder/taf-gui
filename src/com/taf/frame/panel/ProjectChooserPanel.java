package com.taf.frame.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.taf.event.ProjectOpenedEvent;
import com.taf.exception.ParseException;
import com.taf.frame.ProjectFrame;
import com.taf.frame.dialog.ProjectCreationDialog;
import com.taf.logic.field.Root;
import com.taf.manager.ConstantManager;
import com.taf.manager.EventManager;
import com.taf.manager.SaveManager;

public class ProjectChooserPanel extends JPanel {

	private static final long serialVersionUID = 6815040547237393654L;

	public ProjectChooserPanel() {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

		GridBagConstraints c = ConstantManager.getDefaultConstraint();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		DefaultTableModel tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 6314398563329275218L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableModel.setColumnCount(1);
		tableModel.setColumnIdentifiers(new String[] { "Projects" });

		for (String projectName : SaveManager.getInstance().getProjectNames()) {
			tableModel.addRow(new String[] { projectName });
		}
		JTable projectTable = new JTable(tableModel);
		projectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(projectTable);
		this.add(scrollPane, c);

		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(10, 0, 0, 5);
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridy = 1;
		JButton createButton = new JButton("Create new project");
		createButton.addActionListener(e -> {
			ProjectCreationDialog dialog = new ProjectCreationDialog();
			dialog.initDialog();
			String projectName = dialog.getProjectName();

			if (projectName != null) {
				try {
					// TODO Sanitize input
					if (SaveManager.getInstance().createProject(projectName)) {
						tableModel.addRow(new String[] { projectName });
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.add(createButton, c);

		c.insets = new Insets(10, 5, 0, 0);
		c.gridx = 1;
		// TODO Disable button when nothing is selected
		JButton openButton = new JButton("Open project");
		openButton.addActionListener(e -> {
			int row = projectTable.getSelectedRow();
			if (row != -1) {
				String projectName = (String) tableModel.getValueAt(row, 0);
				try {
					Root root = SaveManager.getInstance().openProject(projectName);
					ProjectFrame frame = new ProjectFrame(root);
					frame.initFrame();
					EventManager.getInstance().fireEvent(new ProjectOpenedEvent());
				} catch (IOException | ParseException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.add(openButton, c);
		
		// TODO Add pop-up menu to remove saves on right click
	}

}
