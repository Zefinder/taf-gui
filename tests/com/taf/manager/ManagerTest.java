package com.taf.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.taf.util.OSValidator;

abstract class ManagerTest {

	private static boolean initFail = false;

	protected String mainDirectory;

	@BeforeEach
	void setUpManagers() {
		Manager.initManagers();

		OSValidator OS = OSValidator.getOS();
		switch (OS) {
		case WINDOWS:
			mainDirectory = SaveManager.WINDOWS_USER_BASE_MAIN_DIR;
			break;

		case UNIX:
		case MAC:
			mainDirectory = SaveManager.UNIX_MAC_USER_BASE_MAIN_DIR;
			break;

		default:
			mainDirectory = SaveManager.OTHER_USER_BASE_MAIN_DIR;
		}
		mainDirectory += File.separator + SaveManager.TAF_DIRECTORY_NAME;

		File mainDirectoryFile = new File(mainDirectory);
		if (mainDirectoryFile.exists()) {
			mainDirectoryFile.renameTo(new File(mainDirectory + "_tmp"));
		}

		mainDirectoryFile.mkdir();
		initFail = false;
	}

	@AfterEach
	void clearUpManagers() throws IOException {
		Manager.clearManagers();

		if (initFail) {
			return;
		}

		// Delete the test files
		File mainDirectoryFile = new File(mainDirectory);
		try (Stream<Path> paths = Files.walk(mainDirectoryFile.toPath())) {
			paths.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
		}

		// Replace the old one
		mainDirectoryFile = new File(mainDirectory + "_tmp");
		mainDirectoryFile.renameTo(new File(mainDirectory));
	}

}
