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

public class SettingsDialog extends JDialog {

	private static final long serialVersionUID = 4489590104263838991L;

	private static final String DIALOG_TITLE = "Path settings";
	private static final String TAF_PATH_LABEL_TEXT = "Taf src path:";
	private static final String TAF_PATH_BUTTON_TEXT = "...";

	public SettingsDialog() {
		this.setTitle(DIALOG_TITLE);
		this.setModalityType(ModalityType.APPLICATION_MODAL);

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = Consts.getDefaultConstraint();
		c.weightx = 0;
		c.insets = new Insets(Consts.LARGE_INSET_GAP, Consts.MEDIUM_INSET_GAP, Consts.LARGE_INSET_GAP, Consts.SMALL_INSET_GAP);
		JLabel tafPathLabel = new JLabel(TAF_PATH_LABEL_TEXT);
		this.add(tafPathLabel, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.insets = new Insets(Consts.LARGE_INSET_GAP, Consts.SMALL_INSET_GAP, Consts.LARGE_INSET_GAP, Consts.SMALL_INSET_GAP);
		JTextField tafPathField = new JTextField(Consts.JTEXT_FIELD_DEFAULT_COLUMN);
		tafPathField.setEditable(false);
		tafPathField.setText(SettingsManager.getInstance().getTafDirectory());
		this.add(tafPathField, c);

		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(Consts.LARGE_INSET_GAP, Consts.SMALL_INSET_GAP, Consts.LARGE_INSET_GAP, Consts.MEDIUM_INSET_GAP);
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

	public void initDialog() {
		this.setVisible(true);
	}

}
