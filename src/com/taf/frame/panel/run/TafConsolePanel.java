package com.taf.frame.panel.run;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.taf.event.EventListener;
import com.taf.event.EventMethod;
import com.taf.event.ProcessReadyEvent;
import com.taf.manager.EventManager;
import com.taf.manager.RunManager;
import com.taf.util.Consts;

public class TafConsolePanel extends JPanel implements EventListener {

	private static final long serialVersionUID = -8939892289753953959L;

	private JTextPane textPane;

	public TafConsolePanel() {
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = Consts.getDefaultConstraint();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;

		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setBackground(Consts.CONSOLE_BACKGROUND_COLOR);

		JScrollPane scrollPane = new JScrollPane(textPane);
		this.add(scrollPane, c);

		EventManager.getInstance().registerEventListener(this);
	}

	@EventMethod
	public void onProcessReady(ProcessReadyEvent event) {
		RunManager.getInstance().setTextPane(textPane);
	}

	@Override
	public void unregisterComponents() {
		// No children
	}

}
