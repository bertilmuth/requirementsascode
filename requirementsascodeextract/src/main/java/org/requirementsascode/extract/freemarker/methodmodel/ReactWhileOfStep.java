package org.requirementsascode.extract.freemarker.methodmodel;

import static org.requirementsascode.extract.freemarker.methodmodel.util.Words.getLowerCaseWordsOfClassName;

import java.util.List;

import org.requirementsascode.Step;
import org.requirementsascode.condition.ReactWhile;

import freemarker.ext.beans.BeanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class ReactWhileOfStep implements TemplateMethodModelEx {
  private static final String REACT_WHILE_PREFIX = "As long as ";
  private static final String REACT_WHILE_POSTFIX = ": ";

  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() != 1) {
      throw new TemplateModelException("Wrong number of arguments. Must be 1.");
    }

    Step step = getStep(arguments.get(0));

    String reactWhile = "";
    if (step.getPredicate() instanceof ReactWhile) {
      ReactWhile reactWhilePredicate = (ReactWhile) step.getPredicate();
      reactWhile = REACT_WHILE_PREFIX
          + getLowerCaseWordsOfClassName(getReactWhileConditionClass(reactWhilePredicate)) + REACT_WHILE_POSTFIX;
    }

    return new SimpleScalar(reactWhile);
  }

  private Class<?> getReactWhileConditionClass(ReactWhile reactWhilePredicate) {
    return reactWhilePredicate.getReactWhileCondition().getClass();
  }

  private Step getStep(Object argument) {
    return (Step) ((BeanModel) argument).getAdaptedObject(Step.class);
  }
}
