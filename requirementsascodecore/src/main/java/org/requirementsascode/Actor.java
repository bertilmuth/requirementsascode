package org.requirementsascode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * An actor, as part of a use case model.
 *
 * <p>An actor is a role that a user plays. Actors represent different user groups. They enable to
 * distinguish user rights: only an actor that is connected to a particular use case step is allowed
 * to cause a system reaction for that step.
 *
 * @author b_muth
 */
public class Actor extends UseCaseModelElement implements Serializable {
	private static final long serialVersionUID = 2441478758595877661L;
	
	private Map<UseCase, List<Step>> useCaseToStepMap;

  /**
   * Creates an actor with the specified name that is part of the specified use case model.
   *
   * @param name the name of the actor
   * @param useCaseModel the use case model
   */
  Actor(String name, UseCaseModel useCaseModel) {
    super(name, useCaseModel);
    this.useCaseToStepMap = new HashMap<>();
  }

  /**
   * Returns the use cases this actor is associated with.
   *
   * <p>The actor is associated to a use case if it is connected to at least one of its use case
   * steps.
   *
   * @return the use cases the actor is associated with
   */
  public Set<UseCase> getUseCases() {
    Set<UseCase> useCases = useCaseToStepMap.keySet();
    return Collections.unmodifiableSet(useCases);
  }

  /**
   * Returns the use case steps this actor is connected with, for the specified use case.
   *
   * @param useCase the use case to query for steps the actor is connected with
   * @return the use case steps the actor is connected with
   */
  public List<Step> getStepsOf(UseCase useCase) {
    Objects.requireNonNull(useCase);

    List<Step> steps = getModifiableStepsOf(useCase);
    return Collections.unmodifiableList(steps);
  }

  void newStep(Step useCaseStep) {
    Objects.requireNonNull(useCaseStep.getUseCase());
    Objects.requireNonNull(useCaseStep);

    List<Step> steps = getModifiableStepsOf(useCaseStep.getUseCase());
    steps.add(useCaseStep);
  }

  private List<Step> getModifiableStepsOf(UseCase useCase) {
    useCaseToStepMap.putIfAbsent(useCase, new ArrayList<>());
    return useCaseToStepMap.get(useCase);
  }
}
