package com.taf.util;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.taf.manager.ConstantManager;

public class ProcessStreamReader {

	private JTextPane textPane;
	private StyledDocument document;
	private Style style;

	private AtomicBoolean running;

	public ProcessStreamReader() {
		running = new AtomicBoolean(false);
	}

	public void setTextPane(JTextPane textPane) {
		this.textPane = textPane;
		
		// Setup the underlying document
		document = (StyledDocument) textPane.getDocument();
		
		// Clean the document for the new execution
		try {
			document.remove(0, document.getLength());
		} catch (BadLocationException e) {
			// Can never happen since this is the only place where the document is edited
		}
		
		// Add the style
		document.removeStyle("ConsoleStyle");
		style = document.addStyle("ConsoleStyle", null);
		StyleConstants.setFontFamily(style, "MonoSpaced");
		StyleConstants.setFontSize(style, 12);
	}

	public void start(Process process) {
		Thread inThread = new Thread(() -> runInput(process.getInputStream()));
		inThread.setDaemon(true);
		inThread.setName("Process input stream daemon");

		Thread errThread = new Thread(() -> runError(process.getErrorStream()));
		errThread.setDaemon(true);
		errThread.setName("Process error stream daemon");

		running.set(true);
		inThread.start();
		errThread.start();
	}

	public void stop() {
		running.set(false);
	}

	public boolean isRunning() {
		return running.get();
	}

	public synchronized void runInput(InputStream in) {
		while (running.get()) {
			// Print process input into the system output
			try {
				writeIn(in);
			} catch (IOException | BadLocationException e) {
				ConstantManager.showError("Something went wrong when trying to read the process input stream, abort!");
				running.set(false);
				return;
			}

			try {
				this.wait(100);
			} catch (InterruptedException e) {
				// Nothing interesting here
			}
		}
		
		try {
			writeIn(in);
		} catch (IOException | BadLocationException e) {
			e.printStackTrace();
		}
	}

	public synchronized void runError(InputStream err) {
		while (running.get()) {
			// Print process error into the system error
			try {
				writeErr(err);
			} catch (IOException | BadLocationException e) {
				ConstantManager.showError("Something went wrong when trying to read the process error stream, abort!");
				running.set(false);
				return;
			}

			try {
				this.wait(100);
			} catch (InterruptedException e) {
				// Nothing interesting here
			}
		}
		
		try {
			writeErr(err);
		} catch (IOException | BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private synchronized void writeIn(InputStream in) throws IOException, BadLocationException {
		if (in.available() != 0) {
			// Print in black in the document
			String input = this.readLine(in);
			StyleConstants.setForeground(style, Color.black);
			document.insertString(document.getLength(), input, style);
			textPane.setCaretPosition(textPane.getDocument().getLength());
		}
	}
	
	private synchronized void writeErr(InputStream err) throws IOException, BadLocationException {
		if (err.available() != 0) {
			// Print in red in the document
			String input = this.readLine(err);
			StyleConstants.setForeground(style, Color.red);
			document.insertString(document.getLength(), input, style);
			textPane.setCaretPosition(textPane.getDocument().getLength());
			System.err.println(input);
		}
	}

	private String readLine(InputStream stream) throws IOException {
		String input = "";
		do {
			int available = stream.available();
			if (available == 0) {
				break;
			}
			byte b[] = new byte[available];
			stream.read(b);
			input += new String(b, 0, b.length);
		} while (!input.endsWith("\n") && !input.endsWith("\r\n") && !running.get());
		return input;
	}
}
