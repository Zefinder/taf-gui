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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * The FactoryObject annotation provides information on a type, telling the user
 * and the compiler (TODO) that this object is used in a factory.
 * </p>
 * 
 * <p>
 * This annotation takes two arguments:
 * <ul>
 * <li>An array of classes representing the types that at least one constructor
 * must have.
 * <li>A boolean that is used to tell the processor if the factory code must be
 * generated for this object.
 * </ul>
 * </p>
 * 
 * <p>
 * This annotation is NOT available during runtime and is only useful for
 * documentation purposes.
 * </p>
 * 
 * @author Adrien Jakubiak
 */
@Retention(SOURCE)
@Target(TYPE)
@Documented
public @interface FactoryObject {

	/**
	 * Returns an array of classes that at least one constructor have as arguments.
	 *
	 * @return the array of classes that a constructor must have
	 */
	public String[] types() default {};

	/**
	 * Returns true if the object needs to be generated (WIP)
	 *
	 * @return true if the object needs to be generated
	 */
	public boolean generate() default false;
}
