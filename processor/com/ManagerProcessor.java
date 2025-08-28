package com;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes(value = "com.taf.annotation.ManagerImpl")
public class ManagerProcessor extends AbstractProcessor {

	private static final Set<Modifier> MANAGER_GET_INSTANCE_MODIFIERS = Set.of(Modifier.PUBLIC, Modifier.STATIC);
	private static final Set<Modifier> MANAGER_INSTANCE_MODIFIERS = Set.of(Modifier.PRIVATE, Modifier.STATIC,
			Modifier.FINAL);

	private static final String GET_INSTANCE_METHOD_ERROR = "A manager should implement a public static final method named getInstance";
	private static final String GET_INSTANCE_METHOD_FINAL_INFO = "The getInstance method should be final";
	private static final String ZERO_INSTANCE_FIELD_WARNING = "A manager should have an instance that is private static final";
	private static final String MULTIPLE_INSTANCE_FIELD_WARNING = "A manager should not have more than one instance";

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (TypeElement annotation : annotations) {
			Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
			annotatedElements.forEach(element -> {
				if (element instanceof TypeElement typeElement) {
////					processingEnv.getMessager().printMessage(Kind.WARNING, element.getKind().toString(), element);
//					for (Element e : typeElement.getEnclosedElements()) {
//						if (e instanceof ExecutableElement executableElement) {
//							if (executableElement.getModifiers().containsAll(MANAGER_GET_INSTANCE_MODIFIERS)) {
//								if (executableElement.getParameters().size() == 0) {
//									if (executableElement.getReturnType().toString().equals(element.toString())) {
//										processingEnv.getMessager().printMessage(Kind.WARNING, element.toString(), e);
//									}
//								}
//							}
//						}
//					}

					List<ExecutableElement> getInstances = typeElement.getEnclosedElements().stream()
							.filter(ExecutableElement.class::isInstance).map(ExecutableElement.class::cast)
							.filter(executableElement -> executableElement.getModifiers()
									.containsAll(MANAGER_GET_INSTANCE_MODIFIERS))
							.filter(executableElement -> executableElement.getParameters().size() == 0)
							.filter(executableElement -> executableElement.getReturnType().toString()
									.equals(element.toString()))
							.collect(Collectors.toList());

					// There cannot be more than one method that correspond to that name and number
					// of parameters
					if (getInstances.size() == 0) {
						processingEnv.getMessager().printError(GET_INSTANCE_METHOD_ERROR, element);
					}

					if (!getInstances.get(0).getModifiers().contains(Modifier.FINAL)) {
						processingEnv.getMessager().printMessage(Kind.OTHER, GET_INSTANCE_METHOD_FINAL_INFO,
								getInstances.get(0));
					}

					long count = typeElement.getEnclosedElements().stream().filter(VariableElement.class::isInstance)
							.map(VariableElement.class::cast)
							.filter(variableElement -> variableElement.getModifiers()
									.containsAll(MANAGER_INSTANCE_MODIFIERS))
							.map(variableElement -> variableElement.asType()).filter(DeclaredType.class::isInstance)
							.map(DeclaredType.class::cast)
							.filter(declaredType -> declaredType.toString().equals(element.toString())).count();

					// There must be only one instance of the manager
					if (count == 0) {
						processingEnv.getMessager().printMessage(Kind.WARNING, ZERO_INSTANCE_FIELD_WARNING, element);
					} else if (count > 1) {
						processingEnv.getMessager().printMessage(Kind.OTHER, MULTIPLE_INSTANCE_FIELD_WARNING, element);
					}

//					long count = typeElement.getEnclosedElements().stream().filter(ExecutableElement.class::isInstance)
//							.map(ExecutableElement.class::cast)
//							.filter(executableElement -> executableElement.getSimpleName().toString()
//									.equals("getInstance"))
//							.filter(executableElement -> MANAGER_GET_INSTANCE_MODIFIERS
//									.containsAll(executableElement.getModifiers()))
//							.filter(executableElement -> executableElement.getParameters().size() == 0)
//							.filter(executableElement -> executableElement.getReturnType().toString()
//									.equals(element.toString()))
//							.count();
//
//					// There cannot be more than one method that correspond to that name and number
//					// of parameters
//					if (count == 0) {
//						processingEnv.getMessager().printError(GET_INSTANCE_METHOD_ERROR, element);
//					}
//
//					count = typeElement.getEnclosedElements().stream().filter(VariableElement.class::isInstance)
//							.map(VariableElement.class::cast)
//							.filter(variableElement -> MANAGER_INSTANCE_MODIFIERS.containsAll(variableElement.getModifiers()))
//							.filter(variableElement -> variableElement.getConstantValue().getClass().getName().equals(element.toString()))
//							.count();
//					
//					// There must be only one instance of the manager
//					if (count != 1) {
//						processingEnv.getMessager().printMessage(Kind.WARNING, INSTANCE_FIELD_WARNING, element);
//					}
				}
			});
		}

		return false;
	}

}
