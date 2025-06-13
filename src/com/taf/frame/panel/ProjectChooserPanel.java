package com.taf.frame.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
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
	private static final String PROJECT_COLUMN_NAME = "Projects";
	private static final int PROJECT_COLUMN_INDEX = 0;
	private static final String[] COLUMN_IDENTIFIERS = new String[] { PROJECT_COLUMN_NAME };

	private static final String CREATE_PROJECT_BUTTON_TEXT = "Create new project";
	private static final String OPEN_PROJECT_BUTTON_TEXT = "Open project";

	public ProjectChooserPanel() {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(ConstantManager.MEDIUM_INSET_GAP, ConstantManager.XXL_INSET_GAP,
				ConstantManager.MEDIUM_INSET_GAP, ConstantManager.XXL_INSET_GAP));

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

		tableModel.setColumnCount(COLUMN_IDENTIFIERS.length);
		tableModel.setColumnIdentifiers(COLUMN_IDENTIFIERS);

		for (String projectName : SaveManager.getInstance().getProjectNames()) {
			tableModel.addRow(new String[] { projectName });
		}
		JTable projectTable = new JTable(tableModel);
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
			}

			public void mouseReleased(MouseEvent e) {
				int row = selectRow(e);

				if (row != -1) {
					projectTable.setRowSelectionInterval(row, row);
//						tree.setSelectionRow(selRow);
//						DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
//						
//						NodeObject nodeObject = (NodeObject) node.getUserObject();
//						Entity entity = nodeObject.getEntity();
//						
//						// Show popup if not root
//						if (!(entity instanceof Root)) {
//							JPopupMenu menu = new TreeEntityPopupMenu(entity);
//							menu.show(tree, x, y);
//						}

				}
			}
		};
		projectTable.addMouseListener(rightClickListener);
		projectTable.addMouseMotionListener(rightClickListener);

		JScrollPane scrollPane = new JScrollPane(projectTable);
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
		openButton.addActionListener(e -> {
			int row = projectTable.getSelectedRow();
			if (row != -1) {
				String projectName = (String) tableModel.getValueAt(row, PROJECT_COLUMN_INDEX);
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
