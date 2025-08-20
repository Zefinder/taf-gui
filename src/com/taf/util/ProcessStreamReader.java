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
package com.taf.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.taf.event.ProjectRunSpecialErrorEvent;
import com.taf.event.ProjectRunSpecialErrorEvent.ErrorType;
import com.taf.manager.EventManager;

/**
 * The ProcessStreamReader gives a way to display Python style colored messages
 * in a JTextPane from another process.
 *
 * @author Adrien Jakubiak
 */
public class ProcessStreamReader {

	/** The text pane that will act as a console. */
	private JTextPane textPane;

	/** The text pane document. */
	private StyledDocument document;

	/** The document style. */
	private Style style;

	/** The running state. */
	private AtomicBoolean running;

	/**
	 * Instantiates a new process stream reader with a running state of false.
	 */
	public ProcessStreamReader() {
		running = new AtomicBoolean(false);
	}

	/**
	 * Checks if the running state is true.
	 *
	 * @return true if the stream reader is running
	 */
	public boolean isRunning() {
		return running.get();
	}

	/**
	 * Sets the text pane and initializes its document.
	 *
	 * @param textPane the new text pane
	 */
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
		setStyle(ConsoleStyle.RESET_ALL);
	}

	/**
	 * Starts the stream reader with the given process. This will set the running
	 * state to true.
	 *
	 * @param process the process to read from
	 */
	public void start(Process process) {
		Thread inThread = new Thread(() -> runInput(process.getInputStream()));
		inThread.setDaemon(false);
		inThread.setName("Process input stream daemon");

		Thread errThread = new Thread(() -> runError(process.getErrorStream()));
		errThread.setDaemon(true);
		errThread.setName("Process error stream daemon");

		// Write Y to the stream to create the folders
		try (BufferedOutputStream out = new BufferedOutputStream(process.getOutputStream())) {
			out.write('Y');
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		running.set(true);
		inThread.start();
		errThread.start();
	}

	/**
	 * Stops the stream reader. This will set the running state to false.
	 */
	public void stop() {
		running.set(false);
	}

	/**
	 * Reads a line from a stream.
	 *
	 * @param stream the stream
	 * @return the line from the stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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

	/**
	 * Runs the stream reader thread for error inputs.
	 *
	 * @param err the error stream
	 */
	private synchronized void runError(InputStream err) {
		while (running.get()) {
			// Print process error into the system error
			try {
				writeErr(err);
			} catch (IOException | BadLocationException e) {
				Consts.showError("Something went wrong when trying to read the process error stream, abort!");
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

	/**
	 * Runs the stream reader thread for normal inputs.
	 *
	 * @param in the input stream
	 */
	private synchronized void runInput(InputStream in) {
		while (running.get()) {
			// Print process input into the system output
			try {
				writeIn(in);
			} catch (IOException | BadLocationException e) {
				Consts.showError("Something went wrong when trying to read the process input stream, abort!");
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

	/**
	 * Sets the document style.
	 *
	 * @param consoleStyle the new style
	 */
	private void setStyle(ConsoleStyle consoleStyle) {
		switch (consoleStyle) {
		case FG_BLACK:
			StyleConstants.setForeground(style, Consts.BLACK_COLOR);
			break;

		case FG_RED:
			StyleConstants.setForeground(style, Consts.RED_COLOR);
			break;

		case FG_GREEN:
			StyleConstants.setForeground(style, Consts.GREEN_COLOR);
			break;

		case FG_YELLOW:
			StyleConstants.setForeground(style, Consts.YELLOW_COLOR);
			break;

		case FG_BLUE:
			StyleConstants.setForeground(style, Consts.BLUE_COLOR);
			break;

		case FG_MAGENTA:
			StyleConstants.setForeground(style, Consts.MAGENTA_COLOR);
			break;

		case FG_CYAN:
			StyleConstants.setForeground(style, Consts.CYAN_COLOR);
			break;

		case FG_WHITE:
			StyleConstants.setForeground(style, Consts.WHITE_COLOR);
			break;

		case FG_RESET:
			StyleConstants.setForeground(style, Consts.CONSOLE_FOREGROUND_COLOR);
			break;

		case BG_BLACK:
			StyleConstants.setBackground(style, Consts.BLACK_COLOR);
			break;

		case BG_RED:
			StyleConstants.setBackground(style, Consts.RED_COLOR);
			break;

		case BG_GREEN:
			StyleConstants.setBackground(style, Consts.GREEN_COLOR);
			break;

		case BG_YELLOW:
			StyleConstants.setBackground(style, Consts.YELLOW_COLOR);
			break;

		case BG_BLUE:
			StyleConstants.setBackground(style, Consts.BLUE_COLOR);
			break;

		case BG_MAGENTA:
			StyleConstants.setBackground(style, Consts.MAGENTA_COLOR);
			break;

		case BG_CYAN:
			StyleConstants.setBackground(style, Consts.CYAN_COLOR);
			break;

		case BG_WHITE:
			StyleConstants.setBackground(style, Consts.WHITE_COLOR);
			break;

		case BG_RESET:
			StyleConstants.setBackground(style, Consts.CONSOLE_BACKGROUND_COLOR);
			break;

		case UNDERLINED:
			StyleConstants.setUnderline(style, true);
			break;

		case RESET_ALL:
			StyleConstants.setForeground(style, Consts.CONSOLE_FOREGROUND_COLOR);
			StyleConstants.setBackground(style, Consts.CONSOLE_BACKGROUND_COLOR);
			StyleConstants.setUnderline(style, false);
			break;
			
		default:
			// Do nothing
			break;
		}

	}

	/**
	 * Writes content from the error stream to the document as error input..
	 *
	 * @param err the error stream
	 * @throws IOException          Signals that an I/O exception has occurred.
	 * @throws BadLocationException the bad location exception
	 */
	private synchronized void writeErr(InputStream err) throws IOException, BadLocationException {
		if (err.available() != 0) {
			// Print in red in the document
			String input = Consts.LINE_JUMP + this.readLine(err);

			// Check for special messages on the error stream
			ProjectRunSpecialErrorEvent event;
			if (input.contains("import Taf")) {
				event = new ProjectRunSpecialErrorEvent(ErrorType.MODULE_NOT_FOUND_ERROR);
				EventManager.getInstance().fireEvent(event);
			}

			setStyle(ConsoleStyle.RESET_ALL);
			setStyle(ConsoleStyle.FG_RED);
			document.insertString(document.getLength(), input, style);
			textPane.setCaretPosition(textPane.getDocument().getLength());
		}
	}

	/**
	 * Writes content from the input stream to the document as normal input.
	 *
	 * @param in the input stream
	 * @throws IOException          Signals that an I/O exception has occurred.
	 * @throws BadLocationException the bad location exception
	 */
	private synchronized void writeIn(InputStream in) throws IOException, BadLocationException {
		if (in.available() != 0) {
			// Print in black in the document
			String input = this.readLine(in);

			// Line contains formatting
			if (input.contains("\033")) {
				while (input.contains("\033")) {
					// Get the index and write what was before with the cached style
					int startFormatIndex = input.indexOf("\033");
					String toWrite = input.substring(0, startFormatIndex);
					document.insertString(document.getLength(), toWrite, style);

					// Get the identifier and set the new style
					input = input.substring(startFormatIndex);
					int endFormatIndex = input.indexOf('m');
					String identifier = input.substring(2, endFormatIndex);
					ConsoleStyle consoleStyle = ConsoleStyle.fromIdentifier(identifier);
					setStyle(consoleStyle);

					// Resize input
					input = input.substring(endFormatIndex + 1);
				}

				// Print the rest if not blank
				if (!input.isBlank()) {
					document.insertString(document.getLength(), input, style);
				}
			} else {
				document.insertString(document.getLength(), input, style);
			}

			textPane.setCaretPosition(textPane.getDocument().getLength());
		}
	}
}
