package com.taf.manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.taf.event.Event;
import com.taf.event.EventListener;
import com.taf.event.EventMethod;

public class EventManager extends Manager {

	private static final EventManager instance = new EventManager();

	private final HashMap<Class<? extends Event>, Set<ListenerObject>> eventListenerMap;

	private EventManager() {
		eventListenerMap = new HashMap<Class<? extends Event>, Set<ListenerObject>>();
	}

	public void registerEventListener(EventListener listener) {
		// Loop over methods, register when annotation found
		Method[] listenerMethods = listener.getClass().getDeclaredMethods();
		for (Method method : listenerMethods) {
			EventMethod eventAnnotation = method.getAnnotation(EventMethod.class);
			if (eventAnnotation != null) {
				// TODO Check async when implemented
				// Check if only one parameter and if it is an Event
				if (method.getParameterCount() == 1) {
					Class<?> parameterType = method.getParameterTypes()[0];
					if (Event.class.isAssignableFrom(parameterType)) {
						// Checked in the condition
						@SuppressWarnings("unchecked")
						Class<? extends Event> parameterEvent = (Class<? extends Event>) parameterType;
						ListenerObject object = new ListenerObject(listener, method);
						eventListenerMap.computeIfAbsent(parameterEvent, t -> new HashSet<ListenerObject>())
								.add(object);
					}
				}
			}
		}
	}

	public void fireEvent(Event event) {
		Set<ListenerObject> listenerSet = eventListenerMap.get(event.getClass());
		if (listenerSet != null) {
			for (ListenerObject object : listenerSet) {
				EventListener listener = object.getListener();
				Method method = object.getMethod();

				try {
					method.invoke(listener, event);
				} catch (IllegalAccessException | InvocationTargetException e) {
					// It should never go here
					e.printStackTrace();
				}
			}
		}
	}

	public void unregisterEventListener(EventListener listener) {
		// TODO remove from map

		// Remove also components
		listener.unregisterComponents();
	}

	public static EventManager getInstance() {
		return instance;
	}

	@Override
	public void initManager() {
		// Nothing to do here
	}

	private static class ListenerObject {

		private EventListener listener;
		private Method method;

		public ListenerObject(EventListener listener, Method method) {
			this.listener = listener;
			this.method = method;
		}

		public EventListener getListener() {
			return listener;
		}

		public Method getMethod() {
			return method;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof ListenerObject)) {
				return false;
			}

			ListenerObject other = (ListenerObject) obj;
			return listener.equals(other.listener) && method.equals(other.method);
		}

		@Override
		public int hashCode() {
			String listenerHash = String.valueOf(listener.hashCode());
			String methodHash = String.valueOf(method.hashCode());
			return (listenerHash + methodHash).hashCode();
		}

	}

}
