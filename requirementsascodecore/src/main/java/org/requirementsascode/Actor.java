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
 * An actor, as part of a model.
 *
 * <p>
 * An actor is a role that a user plays. Actors represent different user groups.
 * They enable to distinguish user rights: only an actor that is connected to a
 * particular step is allowed to cause a system reaction for that step.
 *
 * @author b_muth
 */
public class Actor extends ModelElement implements Serializable {
    private static final long serialVersionUID = 2441478758595877661L;

    private Map<UseCase, List<Step>> useCaseToStepMap;

    /**
     * Creates an actor with the specified name that is part of the specified use
     * case model.
     *
     * @param name
     *            the name of the actor
     * @param model
     *            the model
     */
    Actor(String name, Model model) {
	super(name, model);
	this.useCaseToStepMap = new HashMap<>();
    }

    /**
     * Returns the use cases this actor is associated with.
     *
     * <p>
     * The actor is associated to a use case if it is connected to at least one of
     * its steps.
     *
     * @return the use cases the actor is associated with
     */
    public Set<UseCase> getUseCases() {
	Set<UseCase> useCases = useCaseToStepMap.keySet();
	return Collections.unmodifiableSet(useCases);
    }

    /**
     * Returns the steps this actor is connected with, for the specified
     * use case.
     *
     * @param useCase
     *            the use case to query for steps the actor is connected with
     * @return the steps the actor is connected with
     */
    public List<Step> getStepsOf(UseCase useCase) {
	Objects.requireNonNull(useCase);

	List<Step> steps = getModifiableStepsOf(useCase);
	return Collections.unmodifiableList(steps);
    }

    void newStep(Step step) {
	Objects.requireNonNull(step.getUseCase());
	Objects.requireNonNull(step);

	List<Step> steps = getModifiableStepsOf(step.getUseCase());
	steps.add(step);
    }

    private List<Step> getModifiableStepsOf(UseCase useCase) {
	useCaseToStepMap.putIfAbsent(useCase, new ArrayList<>());
	return useCaseToStepMap.get(useCase);
    }
}
