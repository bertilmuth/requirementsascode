package org.requirementsascode;

import java.util.function.Consumer;

/**
 * Part used by the {@link UseCaseModelBuilder} to build a {@link UseCaseModel}.
 *
 * @see UseCase
 * @author b_muth
 */
public class UseCasePart {
    private UseCase useCase;
    private UseCaseModelBuilder useCaseModelBuilder;
    
    UseCasePart(UseCase useCase, UseCaseModelBuilder useCaseModelBuilder) {
	this.useCase = useCase;
	this.useCaseModelBuilder = useCaseModelBuilder;
    }

    public FlowPart basicFlow() {
	Flow useCaseFlow = useCase().getBasicFlow();
	return new FlowPart(useCaseFlow, this);
    }

    public FlowPart flow(String flowName) {
	Flow useCaseFlow = useCase().newFlow(flowName);
	return new FlowPart(useCaseFlow, this);
    }

    UseCase useCase() {
	return useCase;
    }

    UseCaseModelBuilder useCaseModelBuilder() {
	return useCaseModelBuilder;
    }

    public UseCaseModel build() {
	return useCaseModelBuilder.build();
    }

    public <T> FlowlessUserPart<T> handles(Class<T> eventOrExceptionClass) {
	FlowlessUserPart<T> flowless = new FlowlessUserPart<>(eventOrExceptionClass, 1);
	return flowless;
    }

    class FlowlessUserPart<T> {
	private StepUserPart<T> userPart;
	private long flowlessStepCounter;

	public FlowlessUserPart(Class<T> eventOrExceptionClass, long flowlessStepCounter) {
	    FlowPart anytime = basicFlow().anytime();
	    String stepName = "S" + flowlessStepCounter;
	    StepPart stepPart = new StepPart(anytime.createStep(stepName), anytime);
	    this.userPart = stepPart.handles(eventOrExceptionClass);
	    this.flowlessStepCounter = flowlessStepCounter;
	}

	public FlowlessSystemPart<T> system(Consumer<T> systemReaction) {
	    userPart.system(systemReaction);
	    return new FlowlessSystemPart<>(flowlessStepCounter);
	}
    }
    
    class FlowlessSystemPart<T> {	
	private long flowlessStepCounter;

	public FlowlessSystemPart(long flowlessStepCounter) {
	    this.flowlessStepCounter = flowlessStepCounter;
	}
	
	public <U> FlowlessUserPart<U> handles(Class<U> eventOrExceptionClass) {
	    FlowlessUserPart<U> flowless = new FlowlessUserPart<>(eventOrExceptionClass, ++flowlessStepCounter);
	    return flowless;
	}

	public UseCaseModel build() {
	    return UseCasePart.this.build();
	}
    }
}
