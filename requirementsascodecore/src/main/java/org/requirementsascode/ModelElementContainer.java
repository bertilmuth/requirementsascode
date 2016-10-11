package org.requirementsascode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.requirementsascode.exception.ElementAlreadyInModelException;

class ModelElementContainer {
	private ModelElementContainer() {}
	
	public static <T extends UseCaseModelElement> Optional<T> findModelElement(String modelElementName, Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementName);
		Objects.requireNonNull(modelElementNameToElementMap);
		Optional<T> optionalUseCaseModelElement = modelElementNameToElementMap.containsKey(modelElementName)?
			Optional.of(modelElementNameToElementMap.get(modelElementName)) : Optional.empty();
		return optionalUseCaseModelElement;
	}
	
	public static <T extends UseCaseModelElement> boolean hasModelElement(String modelElementName, Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementName);
		Objects.requireNonNull(modelElementNameToElementMap);
		return findModelElement(modelElementName, modelElementNameToElementMap).isPresent();
	}
	
	public static <T extends UseCaseModelElement> void saveModelElement(T modelElement, Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElement);
		Objects.requireNonNull(modelElementNameToElementMap);
		String modelElementName = modelElement.getName();
		if(hasModelElement(modelElementName, modelElementNameToElementMap)){
			throw new ElementAlreadyInModelException(modelElementName);
		}
		modelElementNameToElementMap.put(modelElementName, modelElement);
	}
	
	public static <T extends UseCaseModelElement> List<T> getModelElements(Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementNameToElementMap);
		ArrayList<T> modelElementList = new ArrayList<>();
		modelElementList.addAll(modelElementNameToElementMap.values());
		return Collections.unmodifiableList(modelElementList);
	}
}
