package org.requirementsascode.extract.freemarker.methodmodel;

import java.util.List;

import org.requirementsascode.Step;
import org.requirementsascode.systemreaction.AbstractContinue;
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
    if(hasSystemReaction(step)) {
      String wordsOfSystemReactionClassName = Words.getLowerCaseWordsOfClassName(getSystemReactionClassName(step));
      String stepNameOrIncludedUseCase = getStepNameOrIncludedUseCase(step);
      systemPartOfStep = "System " + wordsOfSystemReactionClassName + stepNameOrIncludedUseCase + ".";
    }
    return systemPartOfStep;
  }

  private boolean hasSystemReaction(Step step) {
    return !"IgnoreIt".equals(getSystemReactionClassName(step));
  }
  
  private String getStepNameOrIncludedUseCase(Step step) {
    String stepNameOrIncludedUseCase = "";
    if (hasSystemReaction(step)) {
      String name = getSystemReactionClassName(step);
      if ("ContinuesAt".equals(name) || "ContinuesAfter".equals(name) || "ContinuesWithoutAlternativeAt".equals(name)) {
        stepNameOrIncludedUseCase = " " + ((AbstractContinue) step.getSystemReaction()).getStepName();
      } else if ("IncludesUseCase".equals(name)) {
        stepNameOrIncludedUseCase = " " + ((IncludesUseCase) step.getSystemReaction()).getIncludedUseCase().getName();
      }
    }
    return stepNameOrIncludedUseCase;
  }

  private String getSystemReactionClassName(Step step) {
    return step.getSystemReaction().getClass().getSimpleName();
  }

  private Step getStep(Object argument) {
    return (Step) ((BeanModel) argument).getAdaptedObject(Step.class);
  }
}
