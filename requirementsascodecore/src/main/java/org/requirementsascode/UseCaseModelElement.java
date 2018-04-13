package org.requirementsascode;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract base class for elements of a use case model. The main purpose of
 * this class is to create named model elements, and make them part of a use
 * case model.
 *
 * @author b_muth
 */
abstract class UseCaseModelElement implements Serializable {
    private static final long serialVersionUID = 4955023531983786087L;

    private String name;
    private UseCaseModel useCaseModel;

    /**
     * Creates a new element that is a part of the specified use case model.
     *
     * @param name
     *            the name of the element to be created
     * @param useCaseModel
     *            the use case model that will contain the element
     */
    UseCaseModelElement(String name, UseCaseModel useCaseModel) {
	Objects.requireNonNull(name);
	Objects.requireNonNull(useCaseModel);
	this.name = name;
	this.useCaseModel = useCaseModel;
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
     * Returns the use case model that this element is part of.
     *
     * @return the use case model
     */
    public UseCaseModel getUseCaseModel() {
	return useCaseModel;
    }

    @Override
    public String toString() {
	return getName();
    }
}
