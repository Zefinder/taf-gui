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
package com.taf.frame.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.taf.manager.SettingsManager;
import com.taf.util.Consts;

/**
 * The SettingsDialog is shown when the user wants to modify the settings of the
 * applications.
 *
 * @author Adrien Jakubiak
 */
public class SettingsDialog extends JDialog {

	private static final long serialVersionUID = 4489590104263838991L;

	/** The dialog title. */
	private static final String DIALOG_TITLE = "Path settings";

	/** The taf path label text. */
	private static final String TAF_PATH_LABEL_TEXT = "Taf src path:";

	/** The taf path button text. */
	private static final String TAF_PATH_BUTTON_TEXT = "...";

	/**
	 * Instantiates a new settings dialog.
	 */
	public SettingsDialog() {
		this.setTitle(DIALOG_TITLE);
		this.setModalityType(ModalityType.APPLICATION_MODAL);

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = Consts.getDefaultConstraint();
		c.weightx = 0;
		c.insets = new Insets(Consts.LARGE_INSET_GAP, Consts.MEDIUM_INSET_GAP, Consts.LARGE_INSET_GAP,
				Consts.SMALL_INSET_GAP);
		JLabel tafPathLabel = new JLabel(TAF_PATH_LABEL_TEXT);
		this.add(tafPathLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.insets = new Insets(Consts.LARGE_INSET_GAP, Consts.SMALL_INSET_GAP, Consts.LARGE_INSET_GAP,
				Consts.SMALL_INSET_GAP);
		JTextField tafPathField = new JTextField(Consts.JTEXT_FIELD_DEFAULT_COLUMN);
		tafPathField.setEditable(false);
		tafPathField.setText(SettingsManager.getInstance().getTafDirectory());
		this.add(tafPathField, c);

		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(Consts.LARGE_INSET_GAP, Consts.SMALL_INSET_GAP, Consts.LARGE_INSET_GAP,
				Consts.MEDIUM_INSET_GAP);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 2;
		JButton tafPathButton = new JButton(TAF_PATH_BUTTON_TEXT);
		tafPathButton.addActionListener(e -> {
			String tafDirectoryString = chooseDirectory();
			tafPathField.setText(tafDirectoryString);
			SettingsManager.getInstance().setTafDirectory(tafDirectoryString);
		});
		this.add(tafPathButton, c);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}

	/**
	 * Calls the {@link JFileChooser} to select a directory and returns its string
	 * path.
	 *
	 * @return the string path of the chosen directory
	 */
	private String chooseDirectory() {
		JFileChooser directoryChooser = new JFileChooser();
		directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int answer = directoryChooser.showOpenDialog(this);
		if (answer == JFileChooser.APPROVE_OPTION) {
			File selectedDirectory = directoryChooser.getSelectedFile();
			if (selectedDirectory.exists()) {
				return selectedDirectory.getAbsolutePath();
			}
		}

		return "";
	}

	/**
	 * Initializes the dialog.
	 */
	public void initDialog() {
		this.setVisible(true);
	}

}
