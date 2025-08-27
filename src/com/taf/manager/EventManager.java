/*
 * Copyright or © or Copr.
 * 
 * This software is a computer program whose purpose is to generate random test
 * case from a template file describing the data model.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-B license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package com.taf.manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.taf.annotation.EventMethod;
import com.taf.annotation.ManagerImpl;
import com.taf.annotation.Priority;
import com.taf.event.Event;
import com.taf.event.EventListener;

/**
 * <p>
 * The EventManager manager focuses on firing event and registering
 * {@link EventListener}s. It uses the {@link EventMethod} annotation to search
 * for event listening methods. See {@link EventMethod} to understand how to
 * create an event method.
 * </p>
 * 
 * <p>
 * To register an event listener, use
 * {@link #registerEventListener(EventListener)} and to fire an event use
 * {@link #fireEvent(Event)}.
 * </p>
 * 
 * <p>
 * Note that firing events is a synchronous process. The calling object must
 * wait until all listeners finished. Also, the same instance of the fired event
 * is given to all listeners, meaning that modifying an event value will have an
 * unknown behavior.
 * </p>
 * 
 * @see Manager
 * @see EventListener
 * @see Event
 * @see EventMethod
 * 
 * @author Adrien Jakubiak
 */
@ManagerImpl(priority = Priority.HIGH)
public class EventManager implements Manager {

	/** The manager instance. */
	private static final EventManager instance = new EventManager();

	/**
	 * Gets the single instance of EventManager.
	 *
	 * @return single instance of EventManager
	 */
	public static EventManager getInstance() {
		return instance;
	}

	/** The event to listener map. */
	private final Map<Class<? extends Event>, Set<ListenerObject>> eventListenerMap;

	/** The listener to object map. */
	private final Map<EventListener, Set<ListenerObject>> listenerToObjectMap;

	/**
	 * Instantiates a new event manager.
	 */
	private EventManager() {
		eventListenerMap = new HashMap<Class<? extends Event>, Set<ListenerObject>>();
		listenerToObjectMap = new HashMap<EventListener, Set<ListenerObject>>();
	}

	@Override
	public void clear() {
		// Clear all lists and listeners
		eventListenerMap.clear();
		listenerToObjectMap.clear();
	}

	/**
	 * Fire the event. It will call all the listeners that registered for this
	 * event.
	 *
	 * @param event the event to fire
	 */
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

	/**
	 * Checks if there exist event listeners that listen for this event class.
	 *
	 * @param eventClazz the event class
	 * @return true if there are listening event listeners
	 */
	public boolean hasEventListeners(Class<? extends Event> eventClazz) {
		return eventListenerMap.containsKey(eventClazz) && !eventListenerMap.get(eventClazz).isEmpty();
	}

	@Override
	public void init() {
		// Nothing to do here
	}

	/**
	 * Register a new event listener. The registered methods of the
	 * {@link EventListener} must have the {@link EventMethod} annotation and must
	 * have one argument extending {@link Event}.
	 *
	 * @param listener the listener to register
	 */
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

	/**
	 * Unregister the event listener and its internal components.
	 *
	 * @param listener the listener
	 */
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

	/**
	 * Defines a couple of event listener and its listening method for an event.
	 *
	 * @author Adrien Jakubiak
	 */
	private static class ListenerObject {

		/** The event listener. */
		private EventListener listener;

		/** The event class. */
		private Class<? extends Event> event;

		/** The handling method. */
		private Method method;

		/**
		 * Instantiates a new listener object.
		 *
		 * @param listener the listener
		 * @param event    the event
		 * @param method   the method
		 */
		public ListenerObject(EventListener listener, Class<? extends Event> event, Method method) {
			this.listener = listener;
			this.event = event;
			this.method = method;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof ListenerObject)) {
				return false;
			}

			ListenerObject other = (ListenerObject) obj;
			return listener.equals(other.listener) && event.equals(other.event) && method.equals(other.method);
		}

		/**
		 * Returns the event.
		 *
		 * @return the event
		 */
		public Class<? extends Event> getEvent() {
			return event;
		}

		/**
		 * Returns the event listener.
		 *
		 * @return the listener
		 */
		public EventListener getListener() {
			return listener;
		}

		/**
		 * Returns the handling method.
		 *
		 * @return the method
		 */
		public Method getMethod() {
			return method;
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
