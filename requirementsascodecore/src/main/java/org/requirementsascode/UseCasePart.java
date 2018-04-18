package org.requirementsascode;

import java.util.function.Consumer;

import org.requirementsascode.predicate.Anytime;

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

    UseCaseModelBuilder getUseCaseModelBuilder() {
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
	    this.flowlessStepCounter = flowlessStepCounter;
	    StepPart stepPart = createStepPart(eventOrExceptionClass, "S" + flowlessStepCounter);
	    this.userPart = stepPart.handles(eventOrExceptionClass);
	}

	StepPart createStepPart(Class<T> eventOrExceptionClass, String stepName) {
	    FlowStep newStep = useCase.newInterruptingFlowStep(stepName, null, new Anytime(), null);
	    StepPart stepPart = new StepPart(newStep, UseCasePart.this, null);
	    return stepPart;
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
