package org.requirementsascode.extract.freemarker.methodmodel;

import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.getStepFromFreemarker;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Words.getLowerCaseWordsOfClassName;

import java.util.List;

import org.requirementsascode.Condition;
import org.requirementsascode.Step;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class InCasePartOfStep implements TemplateMethodModelEx {
  private static final String IN_CASE = "In case ";

  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() != 1) {
      throw new TemplateModelException("Wrong number of arguments. Must be 1.");
    }

    Step step = getStepFromFreemarker(arguments.get(0));

    String caseCondition = getCaseConditionOfStep(step);

    return new SimpleScalar(caseCondition);
  }

  private String getCaseConditionOfStep(Step step) {    
    String caseCondition = step.getCase()
      .map(this::getCaseCondition)
      .orElse("");
    
    return caseCondition;
  }

  private String getCaseCondition(Condition caseCondition) {
    String conditionWords = IN_CASE + " " + getLowerCaseWordsOfClassName(caseCondition.getClass()) + ", ";
    return conditionWords;
  }
}
