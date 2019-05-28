package org.requirementsascode;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.exception.NoSuchElementInModel;

/**
 * Contains static helper methods to ease the implementation of model creation
 * and finding elements in it, using maps.
 *
 * @author b_muth
 */
class ModelElementContainer {
	static <T extends ModelElement> T findModelElement(String modelElementName,
			Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementName);
		Objects.requireNonNull(modelElementNameToElementMap);

		if (!hasModelElement(modelElementName, modelElementNameToElementMap)) {
			throw new NoSuchElementInModel(modelElementName);
		}
		T modelElement = modelElementNameToElementMap.get(modelElementName);
		return modelElement;
	}

	static <T extends ModelElement> boolean hasModelElement(String modelElementName,
			Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementName);
		Objects.requireNonNull(modelElementNameToElementMap);

		boolean hasModelElement = modelElementNameToElementMap.containsKey(modelElementName);
		return hasModelElement;
	}

	static <T extends ModelElement> void saveModelElement(T modelElement, Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElement);
		Objects.requireNonNull(modelElementNameToElementMap);

		String modelElementName = modelElement.getName();
		if (hasModelElement(modelElementName, modelElementNameToElementMap)) {
			throw new ElementAlreadyInModel(modelElementName);
		}
		modelElementNameToElementMap.put(modelElementName, modelElement);
	}

	static <T extends ModelElement> Collection<T> getModelElements(Map<String, T> modelElementNameToElementMap) {
		Objects.requireNonNull(modelElementNameToElementMap);

		return modelElementNameToElementMap.values();
	}
}
