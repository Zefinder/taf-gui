package com.taf.event;

public interface EventListener {

	/**
	 * Method called after unregistering an {@link EventListener}. This is used to
	 * unregister the attributes if some are also {@link EventListener}s. Leave this
	 * empty if the listener does not have listeners as attributes.
	 */
	void unregisterComponents();

}
