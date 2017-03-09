package org.requirementsascode;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.requirementsascode.exception.ElementAlreadyInModel;

/**
 * Contains static helper methods to ease the implementation of use case model creation 
 * and finding elements in it, using maps.
 * 
 * @author b_muth
 *
 */
class ModelElementContainer {	
	static <T extends UseCaseModelElement> Optional<T> findModelElement(String modelElementName, Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementName);
		Objects.requireNonNull(modelElementNameToElementMap);
		
		Optional<T> useCaseModelElement = modelElementNameToElementMap.containsKey(modelElementName)?
			Optional.of(modelElementNameToElementMap.get(modelElementName)) : Optional.empty();
		return useCaseModelElement;
	}
	
	static <T extends UseCaseModelElement> boolean hasModelElement(String modelElementName, Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementName);
		Objects.requireNonNull(modelElementNameToElementMap);
		
		return findModelElement(modelElementName, modelElementNameToElementMap).isPresent();
	}
	
	static <T extends UseCaseModelElement> void saveModelElement(T modelElement, Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElement);
		Objects.requireNonNull(modelElementNameToElementMap);
		
		String modelElementName = modelElement.name();
		if(hasModelElement(modelElementName, modelElementNameToElementMap)){
			throw new ElementAlreadyInModel(modelElementName);
		}
		modelElementNameToElementMap.put(modelElementName, modelElement);
	}
	
	static <T extends UseCaseModelElement> Collection<T> getModelElements(Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementNameToElementMap);
		
		return modelElementNameToElementMap.values();
	}
}
