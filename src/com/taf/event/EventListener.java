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
package com.taf.event;

import com.taf.annotation.EventMethod;
import com.taf.manager.EventManager;

/**
 * <p>
 * The EventListener represents an object that listens for events. The object
 * created with that class is registered in {@link EventManager} using the
 * object's methods annotated with {@link EventMethod}. When an event is fired,
 * the registered method linked to that event is invoked.
 * </p>
 * 
 * <p>
 * Classes implementing this interface must define the
 * {@link #unregisterComponents()} method. This method is used when a listener
 * is unregistered to also unregister all the listeners it could have created.
 * </p>
 * 
 * <p>
 * An EventListener can listen to any number of event they want, and can even
 * have multiple handler methods for the same event as long as the rules
 * specified in {@link EventMethod} are followed. Here is an example of
 * EventListener:
 * 
 * <pre>
 * public class ListenerTest implements EventListener {
 * 	public ListenerTest() {
 * 		EventListener.getInstance().registerEventListener(this);
 * 	}
 * 
 * 	&#64;EventMethod
 * 	public void onDummyEvent(DummyEvent event) {
 * 		System.out.println("Hello World!");
 * 	}
 * 
 * 	public void unregisterComponents() {
 * 	}
 * 
 * 	public void sayHi() {
 * 		EventManager.getInstance().fireEvent(new DummyEvent());
 * 	}
 * 
 * }
 * </pre>
 * <p>
 * 
 * @see Event
 * @see EventManager
 * @see EventMethod
 * 
 * @author Adrien Jakubiak
 */
public interface EventListener {

	/**
	 * Method called after unregistering an {@link EventListener}. This is used to
	 * unregister the attributes if some are also {@link EventListener}s. Leave this
	 * empty if the listener does not have listeners as attributes.
	 */
	void unregisterComponents();

}
