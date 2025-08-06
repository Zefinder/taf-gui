package com.taf.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

abstract class ManagerTest {

	@BeforeEach
	void setUpManagers() {
		Manager.initAllManagers();
	}

	@AfterEach
	void clearUpManagers() {
		Manager.clearAllManagers();
	}

}
