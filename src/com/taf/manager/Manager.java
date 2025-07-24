package com.taf.manager;

import java.util.Set;

import com.taf.util.HashSetBuilder;

public abstract class Manager {

	private static final Set<Manager> MANAGERS = new HashSetBuilder<Manager>()
			.add(ConstantManager.getInstance())
			.add(TypeManager.getInstance())
			.add(EventManager.getInstance())
			.add(SaveManager.getInstance())
			.add(RunManager.getInstance())
			.add(SettingsManager.getInstance())
			.build();
	
	public Manager() {
	}
	
	public abstract void initManager();
	
	public static final void initAllManagers() {
		for (Manager manager : MANAGERS) {
			manager.initManager();
		}
	}
}
