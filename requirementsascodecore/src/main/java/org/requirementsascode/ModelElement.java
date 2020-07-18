package org.requirementsascode;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract base class for elements of a model. The main purpose of this class
 * is to create named model elements, and make them part of a model.
 *
 * @author b_muth
 */
abstract class ModelElement{
	private String name;
	private Model model;

	/**
	 * Creates a new element that is a part of the specified model.
	 *
	 * @param name  the name of the element to be created
	 * @param model the model that will contain the element
	 */
	ModelElement(String name, Model model) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(model);
		this.name = name;
		this.model = model;
	}

	/**
	 * Returns the name of the element.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the model that this element is part of.
	 *
	 * @return the model
	 */
	public Model getModel() {
		return model;
	}

	@Override
	public String toString() {
		return getName();
	}
}
