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

import com.taf.event.EventListener;
import com.taf.event.EventMethod;
import com.taf.event.ProjectOpenedEvent;
import com.taf.event.ProjectToDeleteEvent;
import com.taf.event.ProjectToImportEvent;
import com.taf.event.ProjectToOpenEvent;
import com.taf.exception.ImportException;
import com.taf.exception.ParseException;
import com.taf.frame.ProjectFrame;
import com.taf.frame.dialog.ProjectCreationDialog;
import com.taf.frame.popup.ProjectImportPopupMenu;
import com.taf.frame.popup.ProjectPopupMenu;
import com.taf.logic.field.Root;
import com.taf.manager.ConstantManager;
import com.taf.manager.EventManager;
import com.taf.manager.SaveManager;

public class ProjectChooserPanel extends JPanel implements EventListener {

	private static final long serialVersionUID = 6815040547237393654L;
	private static final String PROJECT_COLUMN_NAME = "Projects";
	private static final int PROJECT_COLUMN_INDEX = 0;
	private static final String[] COLUMN_IDENTIFIERS = new String[] { PROJECT_COLUMN_NAME };

	private static final String CREATE_PROJECT_BUTTON_TEXT = "Create new project";
	private static final String OPEN_PROJECT_BUTTON_TEXT = "Open project";
	private static final String IMPORT_FILE_CHOOSER_BUTTON = "Import";

	private static final String OPEN_PROJECT_ERROR_MESSAGE = "An error occured when opening the project: ";

	private DefaultTableModel tableModel;
	private JTable projectTable;

	public ProjectChooserPanel() {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(ConstantManager.MEDIUM_INSET_GAP, ConstantManager.XXL_INSET_GAP,
				ConstantManager.MEDIUM_INSET_GAP, ConstantManager.XXL_INSET_GAP));

		GridBagConstraints c = ConstantManager.getDefaultConstraint();
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

			public void mouseReleased(MouseEvent e) {
				int row = selectRow(e);

				if (row != -1) {
					JPopupMenu menu = new ProjectPopupMenu();
					menu.show(projectTable, e.getX(), e.getY());
				}
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
		c.insets = new Insets(ConstantManager.MEDIUM_INSET_GAP, 0, 0, ConstantManager.SMALL_INSET_GAP);
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

		c.insets = new Insets(ConstantManager.MEDIUM_INSET_GAP, ConstantManager.SMALL_INSET_GAP, 0, 0);
		c.gridx = 1;
		// TODO Disable button when nothing is selected
		JButton openButton = new JButton(OPEN_PROJECT_BUTTON_TEXT);
		openButton.addActionListener(e -> openProject());
		this.add(openButton, c);

		EventManager.getInstance().registerEventListener(this);
	}

	private String getSelectedProjectName() {
		int row = projectTable.getSelectedRow();
		if (row != -1) {
			return (String) tableModel.getValueAt(row, PROJECT_COLUMN_INDEX);
		}

		return null;
	}

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
			ConstantManager.showError(OPEN_PROJECT_ERROR_MESSAGE + e1.getMessage());
			e1.printStackTrace();
		} catch (ParseException e1) {
			ConstantManager.showError(OPEN_PROJECT_ERROR_MESSAGE + e1.getShortMessage());
			e1.printStackTrace();
		}
	}

	private void deleteProject() {
		String projectName = getSelectedProjectName();
		if (projectName == null) {
			return;
		}

		SaveManager.getInstance().deleteProject(projectName);
		tableModel.removeRow(projectTable.getSelectedRow());
	}

	@Override
	public void unregisterComponents() {
		// No inner listeners
	}

	@EventMethod
	public void onProjectToOpen(ProjectToOpenEvent event) {
		openProject();
	}

	@EventMethod
	public void onProjectToDelete(ProjectToDeleteEvent event) {
		deleteProject();
	}

	@EventMethod
	public void onProjectToImport(ProjectToImportEvent event) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return ConstantManager.XML_FILE_EXTENSION;
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(ConstantManager.XML_FILE_EXTENSION);
			}
		});
		int answer = chooser.showDialog(null, IMPORT_FILE_CHOOSER_BUTTON);

		if (answer == JFileChooser.APPROVE_OPTION) {
			try {
				String fileName = SaveManager.getInstance().importProject(chooser.getSelectedFile());
				tableModel.addRow(new String[] { fileName });
			} catch (ImportException e) {
				ConstantManager.showError(e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
