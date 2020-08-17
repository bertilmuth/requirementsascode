package org.requirementsascode.builder;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;
import org.requirementsascode.UseCase;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @see UseCase
 * @author b_muth
 */
public class StepUseCasePart {
  private UseCasePart useCasePart;
	
	public StepUseCasePart(UseCasePart useCasePart) {
	  this.useCasePart = useCasePart;
  }

  static StepUseCasePart stepUseCasePart(UseCasePart useCasePart) {
		return new StepUseCasePart(useCasePart);
	}

	/**
	 * Start the "happy day scenario" where all is fine and dandy.
	 * 
	 * @return the flow part to create the steps of the basic flow.
	 */
	public FlowPart basicFlow() {
		return useCasePart.basicFlow();
	}

	/**
	 * Start a flow with the specified name.
	 * 
	 * @param flowName the name of the flow.
	 * 
	 * @return the flow part to create the steps of the flow.
	 */
	public FlowPart flow(String flowName) {
    return useCasePart.flow(flowName);
	}

	/**
	 * Define a default actor that will be used for each step of the use case,
	 * unless it is overwritten by specific actors for the steps (with
	 * <code>as</code>).
	 * 
	 * @param defaultActor the actor to use as a default for the use case's steps
	 * @return this use case part
	 */
	public StepUseCasePart as(AbstractActor defaultActor) {
		useCasePart.as(defaultActor);
		return this;
	}

	/**
	 * Returns the model that has been built.
	 * 
	 * @return the model
	 */
	public Model build() {
    return useCasePart.build();
	}
}
