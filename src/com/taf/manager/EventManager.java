package com.taf.manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.taf.event.Event;
import com.taf.event.EventListener;
import com.taf.event.EventMethod;

public class EventManager extends Manager {

	private static final EventManager instance = new EventManager();

	private final Map<Class<? extends Event>, Set<ListenerObject>> eventListenerMap;
	private final Map<EventListener, Set<ListenerObject>> listenerToObjectMap;
	
	private EventManager() {
		eventListenerMap = new HashMap<Class<? extends Event>, Set<ListenerObject>>();
		listenerToObjectMap = new HashMap<EventListener, Set<ListenerObject>>();
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
						ListenerObject object = new ListenerObject(listener, parameterEvent, method);
						eventListenerMap.computeIfAbsent(parameterEvent, t -> new HashSet<ListenerObject>())
								.add(object);
						listenerToObjectMap.computeIfAbsent(listener, t -> new HashSet<ListenerObject>()).add(object);
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
		// Verify if EventListener has events. A listener can have no event if it is
		// just an intermediate
		if (listenerToObjectMap.containsKey(listener)) {
			// Get listener objects from the listener and remove from event listener map
			// Don't forget to also remove from the listener to object map
			Set<ListenerObject> listenerObjects = listenerToObjectMap.get(listener);
			for (ListenerObject listenerObject : listenerObjects) {
				eventListenerMap.get(listenerObject.getEvent()).remove(listenerObject);
			}
			listenerToObjectMap.remove(listener);

		}

		// Remove also components
		listener.unregisterComponents();
	}
	
	public boolean hasEventListeners() {
		return !eventListenerMap.isEmpty();
	}
	
	public boolean hasEventListeners(Class<? extends Event> eventClazz) {
		return eventListenerMap.containsKey(eventClazz) && !eventListenerMap.get(eventClazz).isEmpty();
	}
	
	public static EventManager getInstance() {
		return instance;
	}

	@Override
	public void initManager() {
		// Nothing to do here
	}
	
	@Override
	public void clearManager() {
		// Clear all lists and listeners
		eventListenerMap.clear();
		listenerToObjectMap.clear();
	}

	private static class ListenerObject {

		private EventListener listener;
		private Class<? extends Event> event;
		private Method method;

		public ListenerObject(EventListener listener, Class<? extends Event> event, Method method) {
			this.listener = listener;
			this.event = event;
			this.method = method;
		}

		public EventListener getListener() {
			return listener;
		}

		public Class<? extends Event> getEvent() {
			return event;
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
			return listener.equals(other.listener) && event.equals(other.event) && method.equals(other.method);
		}

		@Override
		public int hashCode() {
			String listenerHash = String.valueOf(listener.hashCode());
			String eventHash = String.valueOf(event.hashCode());
			String methodHash = String.valueOf(method.hashCode());
			return (listenerHash + eventHash + methodHash).hashCode();
		}

	}

}
