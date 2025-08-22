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
package com.taf.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.taf.event.Event;
import com.taf.event.EventListener;
import com.taf.manager.EventManager;

/**
 * <p>
 * The EventMethod annotation provides information on a method, telling the user
 * and the compiler (TODO) that this method is used as an event handler.
 * </p>
 * 
 * <p>
 * This annotation is processed by the {@link EventManager} in the
 * {@link EventManager#registerEventListener(EventListener)} method. To be
 * recognized as an event method, there are three rules:
 * <ul>
 * <li>The method must be public
 * <li>The method must have one argument
 * <li>The argument must be an {@link Event}
 * </ul>
 * 
 * If one of them is not met, the {@link EventManager} will discard the method.
 * The processor will display a warning if it detects an error in the event
 * method declaration (WIP). Note that if the method has a return type, it will
 * never be used.
 * </p>
 * 
 * <p>
 * This annotation is available during runtime, it is useful for documentation
 * and will be used to register {@link EventListener}s in the
 * {@link EventManager}.
 * </p>
 * 
 * @see EventListener
 * @see EventManager
 * 
 * @author Adrien Jakubiak
 */
@Retention(RUNTIME)
@Target(METHOD)
@Documented
public @interface EventMethod {
	// TODO Add async parameter
}
