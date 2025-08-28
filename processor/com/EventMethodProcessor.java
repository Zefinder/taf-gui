package com;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes(value = "com.taf.annotation.EventMethod")
public class EventMethodProcessor extends AbstractProcessor {

	private static final String PARAMETERS_NUMBER_WARNING = "An event method must have one parameter or it will be ignored";
	private static final String PARAMETERS_TYPE_WARNING_FORMAT = "The parameter must be an Event or it will be ignored (got %s)";
	private static final String PUBLIC_MODIFIER_WARNING = "An event method must be public or it will be ignored";
	private static final String RETURN_TYPE_NOTE = "The return value of an event method is never used";

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (TypeElement annotation : annotations) {
			Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
			annotatedElements.forEach(element -> {
				if (element instanceof ExecutableElement executableElement) {
					ExecutableType executableType = ((ExecutableType) element.asType());
	
					// Tell the user that the number of parameters must be one
					int parameterNumber = executableType.getParameterTypes().size();
					if (parameterNumber == 0 || parameterNumber > 1) {
						processingEnv.getMessager().printMessage(Kind.WARNING, PARAMETERS_NUMBER_WARNING, element);
					}

					// Tell the user that the parameter must be an Event
					TypeMirror parameter = executableType.getParameterTypes().get(0);
					if (parameter.getKind() != TypeKind.DECLARED) {
						processingEnv.getMessager().printMessage(Kind.WARNING,
								PARAMETERS_TYPE_WARNING_FORMAT.formatted(parameter.toString()), executableElement.getParameters().get(0));
					} else {
						if (parameter instanceof DeclaredType parameterType) {
							TypeElement parameterElement = (TypeElement) parameterType.asElement();
							boolean found = false;
							for (TypeMirror interfaceType : parameterElement.getInterfaces()) {
								if (interfaceType.toString().equals("com.taf.event.Event")) {
									found = true;
									break;
								}
							}

							if (!found) {
								processingEnv.getMessager().printMessage(Kind.WARNING,
										PARAMETERS_TYPE_WARNING_FORMAT.formatted(parameterType.toString()), executableElement.getParameters().get(0));
							}
						}
					}

					// Inform the user that the method must be public
					boolean publicFound = false;
					for (Modifier modifier : executableElement.getModifiers()) {
						if (modifier == Modifier.PUBLIC) {
							publicFound = true;
							break;
						}
					}
					
					if (!publicFound) {
						processingEnv.getMessager().printMessage(Kind.WARNING, PUBLIC_MODIFIER_WARNING, element);
					}
					
					// Inform the user that the return type is never used
					if (executableType.getReturnType().getKind() != TypeKind.VOID) {
						processingEnv.getMessager().printMessage(Kind.OTHER, RETURN_TYPE_NOTE, element);
					}
				}
			});
		}
		return false;
	}

}
