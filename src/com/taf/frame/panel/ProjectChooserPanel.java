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
package com.taf.frame.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import com.taf.annotation.EventMethod;
import com.taf.event.EventListener;
import com.taf.event.ProjectOpenedEvent;
import com.taf.event.ProjectToDeleteEvent;
import com.taf.event.ProjectToImportEvent;
import com.taf.event.ProjectToOpenEvent;
import com.taf.exception.ImportException;
import com.taf.exception.ParseException;
import com.taf.frame.MainMenuFrame;
import com.taf.frame.ProjectFrame;
import com.taf.frame.dialog.ProjectCreationDialog;
import com.taf.frame.popup.ProjectImportPopupMenu;
import com.taf.frame.popup.ProjectPopupMenu;
import com.taf.logic.field.Root;
import com.taf.manager.EventManager;
import com.taf.manager.SaveManager;
import com.taf.util.Consts;

/**
 * <p>
 * The PropertyPanel is used in the {@link MainMenuFrame} displays all projects
 * in the main user directory.
 * </p>
 * 
 * <p>
 * All projects are displayed in a JTable, with the possibility to create,
 * delete, open and import new projects.
 * </p>
 * 
 * @see JPanel
 * @see MainMenuFrame
 *
 * @author Adrien Jakubiak
 */
public class ProjectChooserPanel extends JPanel implements EventListener {

	private static final long serialVersionUID = 6815040547237393654L;

	/** The project column name. */
	private static final String PROJECT_COLUMN_NAME = "Projects";

	/** The project column index. */
	private static final int PROJECT_COLUMN_INDEX = 0;

	/** The column identifiers. */
	private static final String[] COLUMN_IDENTIFIERS = new String[] { PROJECT_COLUMN_NAME };

	/** The create project button text. */
	private static final String CREATE_PROJECT_BUTTON_TEXT = "Create new project";

	/** The open project button text. */
	private static final String OPEN_PROJECT_BUTTON_TEXT = "Open project";

	/** The import file chooser button. */
	private static final String IMPORT_FILE_CHOOSER_BUTTON = "Import";

	/** The open project error message. */
	private static final String OPEN_PROJECT_ERROR_MESSAGE = "An error occured when opening the project: ";

	/** The table model. */
	private DefaultTableModel tableModel;

	/** The project table with the names. */
	private JTable projectTable;

	/**
	 * Instantiates a new project chooser panel.
	 */
	public ProjectChooserPanel() {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(Consts.MEDIUM_INSET_GAP, Consts.XXL_INSET_GAP,
				Consts.MEDIUM_INSET_GAP, Consts.XXL_INSET_GAP));

		GridBagConstraints c = Consts.getDefaultConstraint();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 6314398563329275218L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableModel.setColumnCount(COLUMN_IDENTIFIERS.length);
		tableModel.setColumnIdentifiers(COLUMN_IDENTIFIERS);

		for (String projectName : SaveManager.getInstance().getProjectNames()) {
			tableModel.addRow(new String[] { projectName });
		}
		projectTable = new JTable(tableModel);
		projectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		MouseAdapter rightClickListener = new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				selectRow(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				selectRow(e);

				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
					openProject();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				int row = selectRow(e);

				if (row != -1) {
					JPopupMenu menu = new ProjectPopupMenu();
					menu.show(projectTable, e.getX(), e.getY());
				}
			}

			private int selectRow(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					int row = projectTable.rowAtPoint(e.getPoint());

					if (row != -1) {
						projectTable.setRowSelectionInterval(row, row);
						return row;
					}
				}

				return -1;
			}
		};
		projectTable.addMouseListener(rightClickListener);
		projectTable.addMouseMotionListener(rightClickListener);

		JScrollPane scrollPane = new JScrollPane(projectTable);
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					JPopupMenu menu = new ProjectImportPopupMenu();
					menu.show(scrollPane, e.getX(), e.getY());
				}
			}
		});
		this.add(scrollPane, c);

		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(Consts.MEDIUM_INSET_GAP, 0, 0, Consts.SMALL_INSET_GAP);
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridy = 1;
		JButton createButton = new JButton(CREATE_PROJECT_BUTTON_TEXT);
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

		c.insets = new Insets(Consts.MEDIUM_INSET_GAP, Consts.SMALL_INSET_GAP, 0, 0);
		c.gridx = 1;
		// TODO Disable button when nothing is selected
		JButton openButton = new JButton(OPEN_PROJECT_BUTTON_TEXT);
		openButton.addActionListener(e -> openProject());
		this.add(openButton, c);

		EventManager.getInstance().registerEventListener(this);
	}

	/**
	 * Handler for {@link ProjectToDeleteEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onProjectToDelete(ProjectToDeleteEvent event) {
		deleteProject();
	}

	/**
	 * Handler for {@link ProjectToImportEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onProjectToImport(ProjectToImportEvent event) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(Consts.XML_FILE_EXTENSION);
			}

			@Override
			public String getDescription() {
				return Consts.XML_FILE_EXTENSION;
			}
		});
		int answer = chooser.showDialog(null, IMPORT_FILE_CHOOSER_BUTTON);

		if (answer == JFileChooser.APPROVE_OPTION) {
			try {
				String fileName = SaveManager.getInstance().importProject(chooser.getSelectedFile());
				tableModel.addRow(new String[] { fileName });
			} catch (ImportException e) {
				Consts.showError(e.getShortMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Handler for {@link ProjectToOpenEvent}.
	 *
	 * @param event the event
	 */
	@EventMethod
	public void onProjectToOpen(ProjectToOpenEvent event) {
		openProject();
	}

	@Override
	public void unregisterComponents() {
		// No inner listeners
	}

	/**
	 * Deletes the selected project.
	 */
	private void deleteProject() {
		String projectName = getSelectedProjectName();
		if (projectName == null) {
			return;
		}

		SaveManager.getInstance().deleteProject(projectName);
		tableModel.removeRow(projectTable.getSelectedRow());
	}

	/**
	 * Returns the selected project name.
	 *
	 * @return the selected project name
	 */
	private String getSelectedProjectName() {
		int row = projectTable.getSelectedRow();
		if (row != -1) {
			return (String) tableModel.getValueAt(row, PROJECT_COLUMN_INDEX);
		}

		return null;
	}

	/**
	 * Opens the selected project.
	 */
	private void openProject() {
		String projectName = getSelectedProjectName();
		if (projectName == null) {
			return;
		}

		try {
			Root root = SaveManager.getInstance().openProject(projectName);
			ProjectFrame frame = new ProjectFrame(root);
			frame.initFrame();
			EventManager.getInstance().fireEvent(new ProjectOpenedEvent());
		} catch (IOException e1) {
			Consts.showError(OPEN_PROJECT_ERROR_MESSAGE + e1.getMessage());
			e1.printStackTrace();
		} catch (ParseException e1) {
			Consts.showError(OPEN_PROJECT_ERROR_MESSAGE + e1.getShortMessage());
			e1.printStackTrace();
		}
	}

}
