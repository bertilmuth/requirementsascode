package org.requirementsascode.builder;

import static org.requirementsascode.builder.UseCasePart.useCasePart;

import org.requirementsascode.Condition;
import org.requirementsascode.Flow;
import org.requirementsascode.Model;
import org.requirementsascode.Step;
import org.requirementsascode.SystemReaction;
import org.requirementsascode.flowposition.FlowPosition;
import org.requirementsascode.systemreaction.AbstractContinuesAfter;
import org.requirementsascode.systemreaction.ContinuesWithoutAlternativeAt;

/**
 * Class that builds a {@link Model}, in a fluent way.
 *
 * @author b_muth
 */
public class ModelBuilder {
	private static final String HANDLES_MESSAGES = "Handles messages";

	private Model model;

	public ModelBuilder(Model model) {
		this.model = model;
	}

	/**
	 * Only if the specified condition is true, the message is handled.
	 *
	 * @param condition the condition that constrains when the message is handled
	 * @return a part of the builder used to define the message class
	 */
	public FlowlessConditionPart condition(Condition condition) {
		return useCase(HANDLES_MESSAGES).condition(condition);
	}
	
	/**
	 * Creates a named step.
	 * 
	 * @param stepName the name of the created step
	 * @return the created step part
	 */
	public FlowlessStepPart step(String stepName) {
		FlowlessStepPart stepPart = useCase(HANDLES_MESSAGES).step(stepName);
		return stepPart;
	}

	/**
	 * Creates a handler for commands of the specified type.
	 * <p>
	 * Internally, a default use case ("Handles messages") is created in the model.
	 * </p>
	 * 
	 * @param commandClass the specified command class
	 * @param <T>          the type of command
	 * @return a part of the builder used to create the message handler (the "system
	 *         reaction")
	 */
	public <T> FlowlessUserPart<T> user(Class<T> commandClass) {
		FlowlessUserPart<T> userPart = useCase(HANDLES_MESSAGES).user(commandClass);
		return userPart;
	}

	/**
	 * Creates a handler for messages or exceptions of the specified type.
	 * <p>
	 * Internally, a default use case ("Handles messages") is created in the model.
	 * </p>
	 * 
	 * @param messageClass the specified type of messages
	 * @param <T>                   the type of messages
	 * @return a part of the builder used to create the message handler (the "system
	 *         reaction")
	 */
	public <T> FlowlessUserPart<T> on(Class<T> messageClass) {
		FlowlessUserPart<T> userPart = useCase(HANDLES_MESSAGES).on(messageClass);
		return userPart;
	}

	/**
	 * Creates a new use case in the current model, and returns a part for building
	 * its details. If a use case with the specified name already exists, returns a
	 * part for the existing use case.
	 *
	 * @param useCaseName the name of the existing use case / use case to be
	 *                    created.
	 * @return the created / found use case's part.
	 */
	public UseCasePart useCase(String useCaseName) {
		return useCasePart(useCaseName, this);
	}
	
	Model getModel() {
	  return model;
	}

	/**
	 * Returns the model built so far.
	 *
	 * @return the model
	 */
	public Model build() {
	  // This is done lazily, only when building, to enable forward references (#92)
	  resolveFlowPositions();
	  resolveContinuesAfterAndContinuousAt();
	  resolveContinuesWithoutAlternativeAt();
		return getModel();
	}

  private void resolveFlowPositions() {
    model.getUseCases().stream()
	    .flatMap(uc -> uc.getFlows().stream())
	    .map(Flow::getFlowPosition)
	    .filter(fp -> fp != null)
	    .forEach(FlowPosition::resolveStep);
  }
  
  private void resolveContinuesAfterAndContinuousAt() {
    model.getUseCases().stream()
      .flatMap(uc -> uc.getSteps().stream())
      .map(Step::getSystemReaction)
      .filter(sr -> sr != null)
      .map(SystemReaction::getModelObject)
      .filter(obj -> obj instanceof AbstractContinuesAfter)
      .map(obj -> (AbstractContinuesAfter)obj)
      .forEach(AbstractContinuesAfter::resolvePreviousStep);
  }
  
  private void resolveContinuesWithoutAlternativeAt() {
    model.getUseCases().stream()
      .flatMap(uc -> uc.getSteps().stream())
      .map(Step::getSystemReaction)
      .filter(sr -> sr != null)
      .map(SystemReaction::getModelObject)
      .filter(obj -> obj instanceof ContinuesWithoutAlternativeAt)
      .map(obj -> (ContinuesWithoutAlternativeAt)obj)
      .forEach(ContinuesWithoutAlternativeAt::resolveContinueAtStep);
  }
}
