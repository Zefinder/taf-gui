package com.taf.manager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.taf.event.Event;
import com.taf.event.EventListener;
import com.taf.event.EventMethod;

class EventManagerTest extends ManagerTest {

	@BeforeEach
	void checkNoEventListeners() {
		assertFalse(EventManager.getInstance().hasEventListeners(DummyEvent.class));
	}

//	@Test
	void testRegisterEvent() {
		DummyListener listener = new DummyListener();
		EventManager.getInstance().fireEvent(new DummyEvent());
		assertTrue(EventManager.getInstance().hasEventListeners(DummyEvent.class));
		assertEquals(1, listener.executionCounter);
	}

//	@Test
	void testRegisterUnregisterEvent() {
		DummyListener listener = new DummyListener();
		EventManager.getInstance().unregisterEventListener(listener);
		EventManager.getInstance().fireEvent(new DummyEvent());
		assertFalse(EventManager.getInstance().hasEventListeners(DummyEvent.class));
		assertEquals(0, listener.executionCounter);
	}

//	@Test
	void testUnregisterComponents() {
		class InnerListener implements EventListener {
			private int executionCounter;
			private DummyListener dummy;
			
			public InnerListener() {
				executionCounter = 0;
				dummy = new DummyListener();
				EventManager.getInstance().registerEventListener(this);
			}
			
			@Override
			public void unregisterComponents() {
				EventManager.getInstance().unregisterEventListener(dummy);
			}
			
			@EventMethod
			public void onEvent(DummyEvent event) {
				executionCounter++;
			}
		}
		
		InnerListener inner = new InnerListener();
		EventManager.getInstance().fireEvent(new DummyEvent());
		assertEquals(1, inner.executionCounter);
		assertEquals(1, inner.dummy.executionCounter);
		EventManager.getInstance().unregisterEventListener(inner);
		EventManager.getInstance().fireEvent(new DummyEvent());
		assertFalse(EventManager.getInstance().hasEventListeners(DummyEvent.class));
		assertEquals(1, inner.executionCounter);
		assertEquals(1, inner.dummy.executionCounter);
	}
	
//	@Test
	void testBadEventMethods() {
		class BadListener implements EventListener {
			private int executionCounter;
			
			public BadListener() {
				executionCounter = 0;
				EventManager.getInstance().registerEventListener(this);
			}
			
			@Override
			public void unregisterComponents() {
			}
			
			@EventMethod
			public void onEvent1(Object event) {
				executionCounter++;
			}
			
			@SuppressWarnings("unused")
			public void onEvent2(DummyEvent event) {
				executionCounter++;
			}
			
			@EventMethod
			public void onEvent3(DummyEvent event1, DummyEvent event2) {
				executionCounter++;
			}
		}
		
		BadListener bad = new BadListener();
		EventManager.getInstance().fireEvent(new DummyEvent());
		assertFalse(EventManager.getInstance().hasEventListeners(DummyEvent.class));
		assertEquals(0, bad.executionCounter);
	}
	
	private class DummyEvent implements Event {

	}

	private class DummyListener implements EventListener {

		private int executionCounter;

		public DummyListener() {
			executionCounter = 0;
			EventManager.getInstance().registerEventListener(this);
		}

		@Override
		public void unregisterComponents() {
		}

		@EventMethod
		public void onEvent(DummyEvent event) {
			executionCounter++;
		}
	}

}
