package org.requirementsascode;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.requirementsascode.exception.ElementAlreadyInModelException;

/**
 * Contains static helper methods to ease the implementation of use case model creation 
 * and finding elements in it, using maps.
 * 
 * @author b_muth
 *
 */
class ModelElementContainer {
	private ModelElementContainer() {}
	
	public static <T extends UseCaseModelElement> Optional<T> findModelElement(String modelElementName, Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementName);
		Objects.requireNonNull(modelElementNameToElementMap);
		
		Optional<T> useCaseModelElement = modelElementNameToElementMap.containsKey(modelElementName)?
			Optional.of(modelElementNameToElementMap.get(modelElementName)) : Optional.empty();
		return useCaseModelElement;
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
	
	public static <T extends UseCaseModelElement> Collection<T> getModelElements(Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementNameToElementMap);
		
		return modelElementNameToElementMap.values();
	}
}
