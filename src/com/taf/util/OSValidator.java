package com.taf.util;

public enum OSValidator {

	WINDOWS, MAC, UNIX, UNKNOWN;

	private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
	private static final OSValidator OS = getOSFromName(OS_NAME);

	private OSValidator() {
	}

	public static boolean isWindows() {
		return OS_NAME.contains("win");
	}

	public static boolean isMac() {
		return OS_NAME.contains("mac");
	}

	public static boolean isUnix() {
		return (OS_NAME.contains("nix") || OS_NAME.contains("nux") || OS_NAME.contains("aix"));
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