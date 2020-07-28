package org.requirementsascode.extract.freemarker.methodmodel;

import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.getStepFromFreemarker;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Words.getLowerCaseWordsOfClassName;

import java.util.List;

import org.requirementsascode.Step;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class FlowlessCondition implements TemplateMethodModelEx {
  private static final String WHEN = "When ";
  private static final String CONDITION_POSTFIX = ": ";

  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() != 1) {
      throw new TemplateModelException("Wrong number of arguments. Must be 1.");
    }

    Step step = getStepFromFreemarker(arguments.get(0));

    String condition = getConditionWithPostfix(step);

    return new SimpleScalar(condition);
  }

  private String getConditionWithPostfix(Step step) {
    String condition = getCondition(step);
    String sep = condition.isEmpty() ? "" : CONDITION_POSTFIX;
    String conditionWithColon = condition + sep;
    return conditionWithColon;
  }

  private String getCondition(Step step) {
    String conditionWords = step.getCondition().map(
      condition -> (WHEN + getLowerCaseWordsOfClassName(condition.getClass()))).orElse("");
    return conditionWords;
  }
}
