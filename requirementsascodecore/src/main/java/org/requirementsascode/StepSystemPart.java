package org.requirementsascode;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.predicate.After;
import org.requirementsascode.predicate.ReactWhile;

/**
 * Part used by the {@link UseCaseModelBuilder} to build a {@link UseCaseModel}.
 *
 * @see Step#setSystemReaction(Consumer)
 * @author b_muth
 */
public class StepSystemPart<T> {
  private StepPart stepPart;
  private Step step;

  StepSystemPart(StepPart useCaseStepPart, Consumer<T> systemReaction) {
    this.stepPart = useCaseStepPart;
    this.step = useCaseStepPart.getStep();
    step.setSystemReaction(systemReaction);
  }

  public UseCaseModel build() {
    return stepPart.getUseCaseModelBuilder().build();
  }

  /**
   * Creates a new step in this flow, with the specified name, that follows the current step in
   * sequence.
   *
   * @param stepName the name of the step to be created
   * @return the newly created step
   * @throws ElementAlreadyInModel if a step with the specified name already exists in the use case
   */
  public StepPart step(String stepName) {
    FlowPart useCaseFlowPart = stepPart.getFlowPart();
    Flow useCaseFlow = useCaseFlowPart.getUseCaseFlow();

    Step nextStepInFlow =
        useCaseFlow.getUseCase().newStep(stepName, useCaseFlow, Optional.of(step));

    return new StepPart(nextStepInFlow, useCaseFlowPart);
  }

  /**
   * Creates a new flow in the current use case.
   *
   * @param flowName the name of the flow to be created.
   * @return the newly created flow part
   * @throws ElementAlreadyInModel if a flow with the specified name already exists in the use case
   */
  public FlowPart flow(String flowName) {
    Objects.requireNonNull(flowName);

    FlowPart useCaseFlowPart = stepPart.getUseCasePart().flow(flowName);
    return useCaseFlowPart;
  }

  /**
   * Creates a new use case in the current model.
   *
   * @param useCaseName the name of the use case to be created.
   * @return the newly created use case part
   * @throws ElementAlreadyInModel if a use case with the specified name already exists in the model
   */
  public UseCasePart useCase(String useCaseName) {
    Objects.requireNonNull(useCaseName);

    UseCasePart useCasePart = stepPart.getUseCaseModelBuilder().useCase(useCaseName);
    return useCasePart;
  }

  /**
   * React to this step's event as long as the condition is fulfilled.
   *
   * <p>Even when the condition is fulfilled, the flow can advance given that the event of the next
   * step is received.
   *
   * <p>Note that if the condition is not fulfilled after the previous step has been performed, the
   * step will not react at all.
   *
   * @param reactWhileCondition the condition to check
   * @return the system part
   */
  public StepSystemPart<T> reactWhile(Predicate<UseCaseModelRunner> reactWhileCondition) {
    Objects.requireNonNull(reactWhileCondition);

    ReactWhile reactWhile = new ReactWhile(step, reactWhileCondition);
    step.setReactWhile(reactWhile);

    return this;
  }

  public IncludePart<T> include(String useCaseName) {
    UseCase includedUseCase = step.getUseCaseModel().findUseCase(useCaseName);
    IncludePart<T> includePart = new IncludePart<>(includedUseCase, step);
    return includePart;
  }
  
  public class IncludePart<U>{
    private Step lastStepOfIncludedFlow;
    
    private IncludePart(UseCase includedUseCase, Step previousStep) {      
      Flow includedFlow = includedUseCase.getBasicFlow();
      includeFlowAfterStep(includedFlow, previousStep);
      lastStepOfIncludedFlow = getLastStepOf(includedFlow);
    }
    
    private void includeFlowAfterStep(Flow includedFlow, Step previousStep) {
      Predicate<UseCaseModelRunner> oldFlowPosition = includedFlow.getFlowPosition().orElse(r -> true);
      Predicate<UseCaseModelRunner> includeFlowAfterStep =
          oldFlowPosition.or(new After(Optional.of(previousStep)));
      includedFlow.setFlowPosition(includeFlowAfterStep);
    }
    
    private Step getLastStepOf(Flow flow) {
      List<Step> stepsOfFlow = flow.getSteps();
      int lastStepIndex = stepsOfFlow.size() - 1;
      if(lastStepIndex < 0){
        throw new RuntimeException("Included flow \"" + flow.getName() + "\" has no steps!");
      }
      Step lastStepOfFlow = stepsOfFlow.get(lastStepIndex);
      return lastStepOfFlow;
    }
    
    public StepPart step(String stepName) {
      StepPart stepPartAfterInclude = StepSystemPart.this.step(stepName);
      stepPartAfterInclude.getStep().setAfterStepAsDefault(Optional.of(lastStepOfIncludedFlow));

      return stepPartAfterInclude;
    }
    
    public UseCaseModel build() {
      return StepSystemPart.this.build();
    }
    
    public FlowPart flow(String flowName) {
      return StepSystemPart.this.flow(flowName);
    }
    
    public UseCasePart useCase(String useCaseName) {
      return StepSystemPart.this.useCase(useCaseName);
    }
  } 
}
