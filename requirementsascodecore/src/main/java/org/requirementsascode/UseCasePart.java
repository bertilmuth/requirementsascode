package org.requirementsascode;

import java.util.Objects;
import java.util.function.Consumer;

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
     * @param flowName
     *            the name of the flow.
     * 
     * @return the flow part to create the steps of the flow.
     */
    public FlowPart flow(String flowName) {
	Flow useCaseFlow = getUseCase().newFlow(flowName);
	return new FlowPart(useCaseFlow, this);
    }
    
    /**
     * Define a default actor that will be used for each step of the use case,
     * unless it is overwritten by specific actors for the steps (with <code>as</code>).
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

    public <T> FlowlessUserPart<T> on(Class<T> eventOrExceptionClass) {
	ConditionPart conditionPart = condition(null);
	FlowlessUserPart<T> flowlessUserPart = conditionPart.on(eventOrExceptionClass);
	return flowlessUserPart;
    }

    public ConditionPart condition(Condition condition) {
	ConditionPart conditionPart = new ConditionPart(condition, 1);
	return conditionPart;
    }

    public class ConditionPart {
	private long flowlessStepCounter;
	private Condition optionalCondition;

	private ConditionPart(Condition optionalCondition, long flowlessStepCounter) {
	    this.optionalCondition = optionalCondition;
	    this.flowlessStepCounter = flowlessStepCounter;
	}

	public <T> FlowlessUserPart<T> on(Class<T> eventOrExceptionClass) {
	    FlowlessUserPart<T> flowless = new FlowlessUserPart<>(optionalCondition, eventOrExceptionClass, flowlessStepCounter);
	    return flowless;
	}

	public <T> FlowlessSystemPart<ModelRunner> system(Runnable systemReaction) {
	    AutonomousSystemReaction autonomousSystemReaction = new AutonomousSystemReaction(systemReaction);
	    return on(ModelRunner.class).system(autonomousSystemReaction);
	}
	
	public <T> FlowlessSystemPart<ModelRunner> system(Consumer<ModelRunner> modelRunnerConsumer) {
	    AutonomousSystemReaction autonomousSystemReaction = new AutonomousSystemReaction(modelRunnerConsumer);
	    return on(ModelRunner.class).system(autonomousSystemReaction);
	}
    }

    public class FlowlessUserPart<T> {
	private StepUserPart<T> userPart;
	private long flowlessStepCounter;

	private FlowlessUserPart(Condition optionalCondition, Class<T> eventOrExceptionClass,
		long flowlessStepCounter) {
	    this.flowlessStepCounter = flowlessStepCounter;
	    StepPart stepPart = createStepPart(optionalCondition, eventOrExceptionClass, "S" + flowlessStepCounter);
	    this.userPart = stepPart.on(eventOrExceptionClass);
	}

	private StepPart createStepPart(Condition optionalCondition, Class<T> eventOrExceptionClass,
		String stepName) {
	    FlowlessStep newStep = useCase.newFlowlessStep(stepName);
	    newStep.setCondition(optionalCondition);
	    StepPart stepPart = new StepPart(newStep, UseCasePart.this, null);
	    return stepPart;
	}

	public FlowlessSystemPart<T> system(Consumer<T> systemReaction) {
	    userPart.system(systemReaction);
	    return new FlowlessSystemPart<>(flowlessStepCounter);
	}
    }

    public class FlowlessSystemPart<T> {
	private long flowlessStepCounter;

	private FlowlessSystemPart(long flowlessStepCounter) {
	    this.flowlessStepCounter = flowlessStepCounter;
	}

	public ConditionPart condition(Condition condition) {
	    ConditionPart conditionPart = new ConditionPart(condition, ++flowlessStepCounter);
	    return conditionPart;
	}

	public <U> FlowlessUserPart<U> on(Class<U> eventOrExceptionClass) {
	    FlowlessUserPart<U> flowlessUserPart = condition(null).on(eventOrExceptionClass);
	    return flowlessUserPart;
	}

	public Model build() {
	    return UseCasePart.this.build();
	}

	public UseCasePart useCase(String useCaseName) {
	    Objects.requireNonNull(useCaseName);
	    UseCasePart useCasePart = getModelBuilder().useCase(useCaseName);
	    return useCasePart;
	}

    }
}
