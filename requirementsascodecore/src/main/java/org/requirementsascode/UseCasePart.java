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

	/**
	 * Constrains the condition for triggering a system reaction: only if the
	 * specified condition is true, a system reaction can be triggered.
	 *
	 * @param condition the condition that constrains when the system reaction is
	 *                  triggered
	 * @return the created condition part
	 */
	public FlowlessConditionPart condition(Condition condition) {
		FlowlessConditionPart conditionPart = new FlowlessConditionPart(condition, this, 1);
		return conditionPart;
	}

	/**
	 * Defines the type of command objects that will cause a system reaction.
	 *
	 * <p>
	 * The system reacts to objects that are instances of the specified class or
	 * instances of any direct or indirect subclass of the specified class.
	 *
	 * @param commandClass the class of commands the system reacts to
	 * @param <T>          the type of the class
	 * @return the created user part
	 */
	public <T> FlowlessUserPart<T> user(Class<T> commandClass) {
		FlowlessConditionPart conditionPart = condition(null);
		FlowlessUserPart<T> flowlessUserPart = conditionPart.user(commandClass);
		return flowlessUserPart;
	}

	/**
	 * Defines the type of event objects or exceptions that will cause a
	 * system reaction.
	 *
	 * <p>
	 * The system reacts to objects that are instances of the specified class or
	 * instances of any direct or indirect subclass of the specified class.
	 *
	 * @param eventOrExceptionClass the class of events the system reacts to
	 * @param <T>                   the type of the class
	 * @return the created user part
	 */
	public <T> FlowlessUserPart<T> on(Class<T> eventOrExceptionClass) {
		FlowlessConditionPart conditionPart = condition(null);
		FlowlessUserPart<T> flowlessUserPart = conditionPart.on(eventOrExceptionClass);
		return flowlessUserPart;
	}

	/**
	 * Returns the model that has been built.
	 * 
	 * @return the model
	 */
	public Model build() {
		return modelBuilder.build();
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
}
