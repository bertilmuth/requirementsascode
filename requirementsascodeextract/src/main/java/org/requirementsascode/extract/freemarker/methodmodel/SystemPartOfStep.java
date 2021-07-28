package org.requirementsascode.extract.freemarker.methodmodel;

import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.getStepFromFreemarker;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.getSystemActor;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.hasSystemEvent;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.hasSystemReaction;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.hasSystemUser;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Words.getLowerCaseWordsOfClassName;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Behavior;
import org.requirementsascode.Step;
import org.requirementsascode.systemreaction.AbstractContinues;

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
      String systemPublishString = getSystemPublishString(step);
      String publishToActorString = getPublishToActorString(step);
      String stepName = getStepName(step);
      systemPartOfStep = on + systemActorName + " " + systemPublishString + wordsOfSystemReactionClassName
        + publishToActorString + stepName + SYSTEM_POSTFIX;
    }
    return systemPartOfStep;
  }

  private String getOn(Step step) {
    String on = "";

    if (hasSystemUser(step) && !hasSystemEvent(step)) {
      on = ON_PREFIX + step.getMessageClass().getSimpleName() + ON_POSTFIX;
    }
    return on;
  }

  private String getSystemPublishString(Step step) {
    Object systemReaction = step.getSystemReaction().getModelObject();
    String systemPublishString = systemReaction instanceof Function ? "publishes " : "";
    return systemPublishString;
  }

  private String getPublishToActorString(Step step) {
    Optional<Behavior> optionalPublishToActor = step.getPublishTo();
    String publishToString = optionalPublishToActor
      .filter(b -> b instanceof AbstractActor)
      .map(b -> (AbstractActor)b)
      .map(act -> " to " + act.getName()).orElse("");
    return publishToString;
  }

  private String getWordsOfSystemReactionClassName(Step step) {
    Object systemReaction = step.getSystemReaction().getModelObject();
    Class<?> systemReactionClass = systemReaction.getClass();
    String wordsOfClassName = getLowerCaseWordsOfClassName(systemReactionClass);
    return wordsOfClassName;
  }

  private String getStepName(Step step) {
    String stepName = "";
    if (hasSystemReaction(step)) {
      Object systemReaction = step.getSystemReaction().getModelObject();
      if (systemReaction instanceof AbstractContinues) {
        stepName = " " + ((AbstractContinues<?>) systemReaction).getStepName();
      }
    }
    return stepName;
  }
}
