package org.requirementsascode.extract.freemarker.methodmodel;

import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.getStepFromFreemarker;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.getSystemActor;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.hasSystemEvent;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.hasSystemReaction;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.hasSystemUser;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Words.getLowerCaseWordsOfClassName;

import java.util.List;
import java.util.function.Consumer;

import org.requirementsascode.AutonomousSystemReaction;
import org.requirementsascode.Step;
import org.requirementsascode.systemreaction.AbstractContinues;
import org.requirementsascode.systemreaction.IncludesUseCase;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class SystemPartOfStep implements TemplateMethodModelEx {
    private static final String ON_PREFIX = "On ";
    private static final String ON_POSTFIX = ": ";
    private static final String SYSTEM_POSTFIX = ".";

    @SuppressWarnings("rawtypes")
    @Override
    public Object exec(List arguments) throws TemplateModelException {
	if (arguments.size() != 1) {
	    throw new TemplateModelException("Wrong number of arguments. Must be 1.");
	}

	Step step = getStepFromFreemarker(arguments.get(0));

	String systemPartOfStep = getSystemPartOfStep(step);

	return new SimpleScalar(systemPartOfStep);
    }

    private String getSystemPartOfStep(Step step) {
	String systemPartOfStep = "";
	if (hasSystemReaction(step)) {
	    String on = getOn(step);
	    String systemActorName = getSystemActor(step).getName();
	    String wordsOfSystemReactionClassName = getWordsOfSystemReactionClassName(step);
	    String stepNameOrIncludedUseCase = getStepNameOrIncludedUseCase(step);
	    systemPartOfStep = on + systemActorName + " " + wordsOfSystemReactionClassName + stepNameOrIncludedUseCase
		    + SYSTEM_POSTFIX;
	}
	return systemPartOfStep;
    }

    private String getWordsOfSystemReactionClassName(Step step) {
	Consumer<?> systemReaction = step.getSystemReaction();
	Class<?> systemReactionClass = systemReaction.getClass();

	if (systemReaction instanceof AutonomousSystemReaction) {
	    AutonomousSystemReaction autonomousSystemReaction = (AutonomousSystemReaction) systemReaction;
	    systemReactionClass = autonomousSystemReaction.getWrappedRunnable().getClass();
	}
	String wordsOfClassName = getLowerCaseWordsOfClassName(systemReactionClass);
	return wordsOfClassName;
    }

    private String getOn(Step step) {
	String on = "";

	if (hasSystemUser(step) && !hasSystemEvent(step)) {
	    on = ON_PREFIX + step.getEventClass().getSimpleName() + ON_POSTFIX;
	}
	return on;
    }

    private String getStepNameOrIncludedUseCase(Step step) {
	String stepNameOrIncludedUseCase = "";
	if (hasSystemReaction(step)) {
	    Consumer<?> systemReaction = step.getSystemReaction();
	    if (systemReaction instanceof AbstractContinues) {
		stepNameOrIncludedUseCase = " " + ((AbstractContinues) systemReaction).getStepName();
	    } else if (systemReaction instanceof IncludesUseCase) {
		stepNameOrIncludedUseCase = " " + ((IncludesUseCase) systemReaction).getIncludedUseCase().getName();
	    }
	}
	return stepNameOrIncludedUseCase;
    }
}
