package com.taf.frame.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;

import com.taf.util.Consts;

public abstract class InputInformationDialog extends JDialog implements KeyListener, ActionListener {

	private static final long serialVersionUID = -2718172025186345523L;

	private static final String OK_BUTTON_TEXT = "Add";

	private JButton okButton;
	private int lastRow;
	private int lastColumn;

	public InputInformationDialog() {
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setLayout(new GridBagLayout());
		this.addKeyListener(this);
		okButton = new JButton(OK_BUTTON_TEXT);
		okButton.addActionListener(this);
		lastRow = 0;
		lastColumn = 0;
	}

	protected void addComponent(JComponent component, GridBagConstraints c) {
		this.add(component, c);
		component.addKeyListener(this);
		if (c.gridy + c.gridheight > lastRow) {
			lastRow = c.gridy + c.gridheight;
		}

		if (c.gridx + c.gridwidth > lastColumn) {
			lastColumn = c.gridx + c.gridwidth;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		performAction();
	}

	protected abstract void performAction();

	public void initDialog() {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(Consts.MEDIUM_INSET_GAP, 0, Consts.HUGE_INSET_GAP, 0);
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = lastColumn;
		c.gridx = 0;
		c.gridy = lastRow;
		this.add(okButton, c);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		switch (e.getKeyChar()) {
		case KeyEvent.VK_ENTER:
			performAction();
			break;

		default:
			break;
		}
	}
}
