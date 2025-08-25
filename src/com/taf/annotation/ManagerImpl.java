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
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * The ManagerImpl annotation tells the coder that the annotated type is a
 * Manager and must be treated as is. A class implementing this annotation must
 * have no public constructor, a static method <code>getInstance</code> that
 * returns the singleton, and two public void methods <code>init()</code> and
 * <code>clear()</code>. Note that a manager must be in com.taf.manager in order
 * to be detected.
 * </p>
 * 
 * <p>
 * Managers are organized in priority: LOW, MEDIUM, HIGH. The higher the
 * priority, the faster they need to be initialized.
 * </p>
 * 
 * <p>
 * This annotation is available during runtime. It is useful for documentation
 * purposes and to perform a check at launch.
 * </p>
 * 
 * @author Adrien Jakubiak
 */
@Retention(RUNTIME)
@Target(TYPE)
@Documented
public @interface ManagerImpl {
	public Priority priority();
}
