package org.requirementsascode.extract.freemarker.methodmodel;

import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.getStepFromFreemarker;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.getSystemActor;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.hasSystemEvent;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.hasSystemReaction;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.hasSystemUser;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Words.getLowerCaseWordsOfClassName;

import java.util.List;
import java.util.function.Consumer;

import org.requirementsascode.Step;
import org.requirementsascode.systemreaction.AbstractContinue;
import org.requirementsascode.systemreaction.IncludesUseCase;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class SystemPartOfStep implements TemplateMethodModelEx {
  private static final String HANDLES_PREFIX = "Handles ";
  private static final String HANDLES_POSTFIX = ": ";
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
      String handles = getHandles(step);
      String systemActorName = getSystemActor(step).getName();
      String wordsOfSystemReactionClassName = getLowerCaseWordsOfClassName(step.getSystemReaction().getClass());
      String stepNameOrIncludedUseCase = getStepNameOrIncludedUseCase(step);
      systemPartOfStep = handles + systemActorName + " " + wordsOfSystemReactionClassName + stepNameOrIncludedUseCase
          + SYSTEM_POSTFIX;
    }
    return systemPartOfStep;
  }

  private String getHandles(Step step) {
    String handles = "";

    if (hasSystemUser(step) && !hasSystemEvent(step)) {
      handles = HANDLES_PREFIX + step.getEventClass().getSimpleName() + HANDLES_POSTFIX;
    }
    return handles;
  }

  private String getStepNameOrIncludedUseCase(Step step) {
    String stepNameOrIncludedUseCase = "";
    if (hasSystemReaction(step)) {
      Consumer<?> systemReaction = step.getSystemReaction();
      if (systemReaction instanceof AbstractContinue) {
        stepNameOrIncludedUseCase = " " + ((AbstractContinue) systemReaction).getStepName();
      } else if (systemReaction instanceof IncludesUseCase) {
        stepNameOrIncludedUseCase = " " + ((IncludesUseCase) systemReaction).getIncludedUseCase().getName();
      }
    }
    return stepNameOrIncludedUseCase;
  }
}
