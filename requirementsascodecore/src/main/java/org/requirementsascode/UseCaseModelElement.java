package org.requirementsascode;

import java.util.Objects;
import java.util.UUID;

/**
 * Abstract base class for elements of a use case model.
 * The main purpose of this class is to create named model elements,
 * and make them part of a use case model.
 * 
 * @author b_muth
 *
 */
abstract class UseCaseModelElement {
	private String name;
	private UseCaseModel useCaseModel;

	/**
	 * Creates a new element that is a part of the specified use case model.
	 * 
	 * @param name the name of the element to be created
	 * @param useCaseModel the use case model that will contain the element
	 */
	public UseCaseModelElement(String name, UseCaseModel useCaseModel) {
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
	
	
	/**
	 * Returns a unique name, based on the specified step name.
	 * 
	 * Purpose of this method is to create unique names for steps
	 * that are "automatically created" in the model, during the
	 * creation of the model.
	 * 
	 * Overwrite this only if you are not happy with the "automatically created"
	 * step names in the model.
	 * 
	 * @param stepName the step name the unique step name is based upon
	 * 
	 * @return a unique step name
	 */
	protected String uniqueStepName(String stepName) {
		return stepNameWithPostfix(stepName, UUID.randomUUID().toString());
	}
	
	/**
	 * Returns a step name, based on the specified step name and the specified postfix
	 * (and brackets and a hashtag in between).
	 * 
	 * Overwrite this only if you are not happy with the "automatically created"
	 * step names in the model.
	 * 
	 * @param stepName the step name the created step name is based upon
	 * 
	 * @param postfix the postfix to append to the specified step name
	 * 
	 * @return the step name with the postfix
	 */
	protected String stepNameWithPostfix(String stepName, String postfix) {
		return stepName + " (#" + postfix.toString() + ")";
	}
}