package org.requirementsascode.extract.freemarker.methodmodel;

import java.util.List;

import org.requirementsascode.Step;
import org.requirementsascode.predicate.ReactWhile;

import freemarker.ext.beans.BeanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class ReactWhileOfStep implements TemplateMethodModelEx {
  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() != 1) {
      throw new TemplateModelException("Wrong number of arguments. Must be 1.");
    }

    Step step = getStep(arguments.get(0));
    
    String reactWhile = "";
    if(step.getPredicate() instanceof ReactWhile) {
      ReactWhile reactWhilePredicate = (ReactWhile)step.getPredicate();
      reactWhile = "As long as " + Words.getLowerCaseWordsOfClassName(getReactWhileConditionClassName(reactWhilePredicate))
        + ": ";
    }

    return new SimpleScalar(reactWhile);
  }

  private String getReactWhileConditionClassName(ReactWhile reactWhilePredicate) {
    return reactWhilePredicate.getReactWhileCondition().getClass().getSimpleName();
  }

  private Step getStep(Object argument) {
    return (Step) ((BeanModel) argument).getAdaptedObject(Step.class);
  }
}
