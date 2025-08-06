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
		setStyle(ConsoleStyle.RESET_ALL);
	}

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

	public synchronized void runError(InputStream err) {
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

		default:
			// Do nothing
			break;
		}

	}
}
