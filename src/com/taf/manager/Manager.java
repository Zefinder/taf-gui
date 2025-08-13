package com.taf.manager;

import java.util.Set;

import com.taf.util.HashSetBuilder;

public abstract class Manager {

	static final Set<Manager> MANAGERS = new HashSetBuilder<Manager>()
			.add(TypeManager.getInstance())
			.add(EventManager.getInstance())
			.add(SaveManager.getInstance())
			.add(RunManager.getInstance())
			.add(SettingsManager.getInstance())
			.build();
	
	private static boolean initDone = false;
	
	public Manager() {
	}
	
	public abstract void initManager();
	
	public abstract void clearManager();
	
	public static final void initAllManagers() {
		if (!initDone) {			
			for (Manager manager : MANAGERS) {
				manager.initManager();
			}
			initDone = true;
		}
	}
	
	public static final void clearAllManagers() {
		if (initDone) {
			for (Manager manager : MANAGERS) {
				manager.clearManager();
			}
			initDone = false;
		}
	}
}
