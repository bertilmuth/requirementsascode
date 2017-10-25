package org.requirementsascode.extract.freemarker.methodmodel;

import java.util.List;
import java.util.function.Consumer;

import org.requirementsascode.Actor;
import org.requirementsascode.Step;
import org.requirementsascode.systemreaction.AbstractContinue;
import org.requirementsascode.systemreaction.IgnoreIt;
import org.requirementsascode.systemreaction.IncludesUseCase;

import freemarker.ext.beans.BeanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class SystemPartOfStep implements TemplateMethodModelEx {
  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() != 1) {
      throw new TemplateModelException("Wrong number of arguments. Must be 1.");
    }

    Step step = getStep(arguments.get(0));

    String systemPartOfStep = getSystemPartOfStep(step);

    return new SimpleScalar(systemPartOfStep);
  }

  private String getSystemPartOfStep(Step step) {
    String systemPartOfStep = "";
    if (hasSystemReaction(step)) {
      String systemReactionClassName = getSystemReactionClassName(step);
      String wordsOfSystemReactionClassName = Words.getLowerCaseWordsOfClassName(systemReactionClassName);
      String stepNameOrIncludedUseCase = getStepNameOrIncludedUseCase(step);
      systemPartOfStep = getSystemActor(step) + " " + wordsOfSystemReactionClassName + stepNameOrIncludedUseCase + ".";
    }
    return systemPartOfStep;
  }

  private boolean hasSystemReaction(Step step) {
    return !(step.getSystemReaction() instanceof IgnoreIt<?>);
  }
  
  private String getSystemReactionClassName(Step step) {
    return step.getSystemReaction().getClass().getSimpleName();
  }

  private String getStepNameOrIncludedUseCase(Step step) {
    String stepNameOrIncludedUseCase = "";
    if (hasSystemReaction(step)) {
      Consumer<?> systemReaction = step.getSystemReaction();
      if (systemReaction instanceof AbstractContinue) {
        stepNameOrIncludedUseCase = " " + ((AbstractContinue)systemReaction).getStepName();
      } else if (systemReaction instanceof IncludesUseCase) {
        stepNameOrIncludedUseCase = " " + ((IncludesUseCase)systemReaction).getIncludedUseCase().getName();
      }
    }
    return stepNameOrIncludedUseCase;
  }

  private Actor getSystemActor(Step step) {
    return step.getUseCaseModel().getSystemActor();
  }

  private Step getStep(Object argument) {
    return (Step) ((BeanModel) argument).getAdaptedObject(Step.class);
  }
}
