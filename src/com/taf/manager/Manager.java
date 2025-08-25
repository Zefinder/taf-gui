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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.taf.annotation.ManagerImpl;

/**
 * <p>
 * A Manager is a class focusing on one aspect of the code. It must be annotated
 * with {@link ManagerImpl}.
 * </p>
 * 
 * @see ManagerImpl
 * 
 * @author Adrien Jakubiak
 */
public interface Manager {

	/** The set of initialized managers. */
	public static Set<Manager> MANAGERS = new HashSet<Manager>();

	/**
	 * Initializes the manager.
	 */
	void init();

	/**
	 * Clears the manager.
	 */
	void clear();

	/**
	 * Initializes all classes with annotation ManagerImpl and having no public
	 * constructors, a static method getInstance that returns itself with no
	 * parameters.
	 */
	public static void initManagers() {
		var annotationClass = ManagerImpl.class;
		String packageName = Manager.class.getPackage().getName();
		InputStream stream = ClassLoader.getSystemResourceAsStream(packageName.replaceAll("[.]", "/"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		reader.lines()
				.filter(line -> line.endsWith(".class"))
				.<Optional<Class<?>>>map(line -> { // Gets all classes within the com.taf.manager package
					try {
						return Optional.of(Class.forName(packageName + "." + line.substring(0, line.lastIndexOf('.'))));
					} catch (ClassNotFoundException e) {
						return Optional.empty();
					}
				})
				.filter(clazzOptional -> clazzOptional.isPresent() // Filters classes that has the annotation ManagerImpl
						&& clazzOptional.get().isAnnotationPresent(annotationClass)) 
				.map(Optional::get)
				.filter(clazz -> clazz.getConstructors().length == 0 // Filters classes with no constructors
						&& Arrays.asList(clazz.getMethods()).stream()
															.anyMatch(method -> method.getName().equals("getInstance") // Method must be named getInstance
																	&& method.getParameterCount() == 0 // with no parameters
																	&& clazz.isAssignableFrom(method.getReturnType()) // that returns itself
																	&& Modifier.isStatic(method.getModifiers()))) // and is static
				.sorted((managerClass1, managerClass2) -> -managerClass1.getAnnotation(annotationClass).priority() // Sort by priority
						.compareTo(managerClass2.getAnnotation(annotationClass).priority()))
				.<Optional<? extends Manager>>map(managerClass -> { // Invoke getInstance
					try {
						return Optional.of((Manager) managerClass.getMethod("getInstance").invoke(null));
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException
							| InvocationTargetException e) {
						// Should never happen
						return Optional.empty();
					}
				})
				.filter(Optional::isPresent).map(Optional::get)
				.forEach(manager -> { // Init manager and add to the manager set
					manager.init();
					MANAGERS.add(manager);
				});
		try {
			reader.close();
		} catch (IOException e) {
			// Ignored
		}
	}

	/**
	 * Clears all managers.
	 */
	public static void clearManagers() {
		for (Manager manager : MANAGERS) {
			manager.clear();
		}

		// Reset the manager set
		MANAGERS.clear();
	}

	public static void main(String[] args) {
		Manager.initManagers();
	}
}
