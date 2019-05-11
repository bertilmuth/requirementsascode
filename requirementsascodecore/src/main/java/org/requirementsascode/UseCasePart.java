package org.requirementsascode;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @see UseCase
 * @author b_muth
 */
public class UseCasePart {
	private UseCase useCase;
	private ModelBuilder modelBuilder;
	private Actor defaultActor;

	UseCasePart(UseCase useCase, ModelBuilder modelBuilder) {
		this.useCase = useCase;
		this.modelBuilder = modelBuilder;
		this.defaultActor = modelBuilder.build().getUserActor();
	}

	/**
	 * Start the "happy day scenario" where all is fine and dandy.
	 * 
	 * @return the flow part to create the steps of the basic flow.
	 */
	public FlowPart basicFlow() {
		Flow useCaseFlow = getUseCase().getBasicFlow();
		return new FlowPart(useCaseFlow, this);
	}

	/**
	 * Start a flow with the specified name.
	 * 
	 * @param flowName the name of the flow.
	 * 
	 * @return the flow part to create the steps of the flow.
	 */
	public FlowPart flow(String flowName) {
		Flow useCaseFlow = getUseCase().newFlow(flowName);
		return new FlowPart(useCaseFlow, this);
	}

	/**
	 * Define a default actor that will be used for each step of the use case,
	 * unless it is overwritten by specific actors for the steps (with
	 * <code>as</code>).
	 * 
	 * @param defaultActor the actor to use as a default for the use case's steps
	 * @return this use case part
	 */
	public UseCasePart as(Actor defaultActor) {
		this.defaultActor = defaultActor;
		return this;
	}

	UseCase getUseCase() {
		return useCase;
	}

	ModelBuilder getModelBuilder() {
		return modelBuilder;
	}

	Actor getDefaultActor() {
		return defaultActor;
	}

	/**
	 * Returns the model that has been built.
	 * 
	 * @return the model
	 */
	public Model build() {
		return modelBuilder.build();
	}

	public <T> FlowlessUserPart<T> on(Class<T> messageClass) {
		FlowlessConditionPart conditionPart = condition(null);
		FlowlessUserPart<T> flowlessUserPart = conditionPart.on(messageClass);
		return flowlessUserPart;
	}

	public FlowlessConditionPart condition(Condition condition) {
		FlowlessConditionPart conditionPart = new FlowlessConditionPart(condition, this, 1);
		return conditionPart;
	}
}
