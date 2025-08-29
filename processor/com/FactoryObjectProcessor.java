package com;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import com.taf.annotation.FactoryObject;

@SupportedAnnotationTypes(value = "com.taf.annotation.FactoryObject")
public class FactoryObjectProcessor extends AbstractProcessor {

	private static final String CONSTRUCTOR_ARGUMENT_ERROR_FORMAT = "The factory object must have a constructor with parameters: %s";

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (TypeElement annotation : annotations) {
			Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
			annotatedElements.forEach(element -> {
				// Get the annotation
				FactoryObject factoryObject = element.getAnnotation(FactoryObject.class);
				String[] constructorParameters = factoryObject.types();

				// Verify that the constructor has the right elements in the right order
				if (element instanceof TypeElement typeElement) {
					Set<ExecutableElement> factoryObjectConstructor = typeElement.getEnclosedElements().stream()
							.filter(enclosedElement -> enclosedElement.getKind() == ElementKind.CONSTRUCTOR)
							.map(ExecutableElement.class::cast)
							.filter(executableElement -> executableElement.getParameters()
									.size() == constructorParameters.length)
							.filter(executableElement -> Arrays.equals(executableElement.getParameters().stream()
									.map(variableElement -> variableElement.asType().toString())
									.toArray(String[]::new), constructorParameters))
							.collect(Collectors.toSet());

					if (factoryObjectConstructor.size() == 0) {
						processingEnv.getMessager().printError(
								CONSTRUCTOR_ARGUMENT_ERROR_FORMAT.formatted(Arrays.toString(constructorParameters)),
								element);
					}
				}
			});
		}

		return false;
	}

}
