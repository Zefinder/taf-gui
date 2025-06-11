package com.taf.util;

public enum OSValidator {

	WINDOWS, MAC, UNIX, UNKNOWN;

	private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
	private static final OSValidator OS = getOSFromName(OS_NAME);

	private static final String WINDOWS_NAME = "win";
	private static final String MAC_NAME = "mac";
	private static final String UNIX1_NAME = "nix";
	private static final String UNIX2_NAME = "nux";
	private static final String UNIX3_NAME = "aix";

	private OSValidator() {
	}

	public static boolean isWindows() {
		return OS_NAME.contains(WINDOWS_NAME);
	}

	public static boolean isMac() {
		return OS_NAME.contains(MAC_NAME);
	}

	public static boolean isUnix() {
		return (OS_NAME.contains(UNIX1_NAME) || OS_NAME.contains(UNIX2_NAME) || OS_NAME.contains(UNIX3_NAME));
	}

	private static OSValidator getOSFromName(String osName) {
		if (isWindows()) {
			return WINDOWS;
		}
		if (isMac()) {
			return MAC;
		}
		if (isUnix()) {
			return UNIX;
		}

		return UNKNOWN;
	}

	public static OSValidator getOS() {
		return OS;
	}

}