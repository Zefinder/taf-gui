package com;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes(value = "com.taf.annotation.EventMethod")
public class EventMethodProcessor extends AbstractProcessor {

	private static final String PARAMETERS_NUMBER_WARNING = "An event method must have one parameter or it will be ignored";
	private static final String PARAMETERS_TYPE_WARNING = "The parameter must be an Event";
	private static final String RETURN_TYPE_NOTE = "The return value of an event method is never used";

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (TypeElement annotation : annotations) {
			Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
			annotatedElements.forEach(element -> {
				ExecutableType executableType = ((ExecutableType) element.asType());
				// Tell the user that the number of parameters must be one
				int parameterNumber = executableType.getParameterTypes().size();
				if (parameterNumber == 0 || parameterNumber > 1) {
					processingEnv.getMessager().printMessage(Kind.WARNING, PARAMETERS_NUMBER_WARNING, element);
				}

				// String className = ((TypeElement)
				// setters.get(0).getEnclosingElement()).getQualifiedName().toString();
				// Tell the user that the parameter must be an Event
				TypeMirror parameter = executableType.getParameterTypes().get(0);
				if (parameter.getKind().isPrimitive() || parameter.getKind() != TypeKind.TYPEVAR) {
					processingEnv.getMessager().printMessage(Kind.WARNING, PARAMETERS_TYPE_WARNING, element);
				} else {
					TypeElement parameterType = (TypeElement) parameter;
					boolean found = false;
					for (TypeMirror interfaceType : parameterType.getInterfaces()) {
						if (interfaceType instanceof TypeElement interfaceElement) {
							if (interfaceElement.getQualifiedName().toString().equals("com.taf.event.Event")) {
								found = true;
								break;
							}
						}
					}
					
					if (!found) {
						processingEnv.getMessager().printMessage(Kind.WARNING, PARAMETERS_TYPE_WARNING, element);
					}
				}

				// Inform the user that the return type is never used
				if (executableType.getReturnType().getKind() != TypeKind.VOID) {
					processingEnv.getMessager().printMessage(Kind.NOTE, RETURN_TYPE_NOTE,
							element.getEnclosingElement());
				}
			});
		}
		return false;
	}

}
