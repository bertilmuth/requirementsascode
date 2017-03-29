package org.requirementsascode;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.exception.NoSuchElementInModel;

/**
 * Contains static helper methods to ease the implementation of use case model creation 
 * and finding elements in it, using maps.
 * 
 * @author b_muth
 *
 */ 
class ModelElementContainer {	
	static <T extends UseCaseModelElement> T findModelElementOrThrow(String modelElementName, Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementName);
		Objects.requireNonNull(modelElementNameToElementMap);
		
		if(!hasModelElement(modelElementName, modelElementNameToElementMap)){
			throw new NoSuchElementInModel(modelElementName);
		}
		T useCaseModelElement = modelElementNameToElementMap.get(modelElementName);
		return useCaseModelElement;
	}
	
	static <T extends UseCaseModelElement> boolean hasModelElement(String modelElementName, Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementName);
		Objects.requireNonNull(modelElementNameToElementMap);
		boolean hasModelElement = modelElementNameToElementMap.containsKey(modelElementName);
		return hasModelElement;
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
