package org.requirementsascode.extract.freemarker.methodmodel;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.requirementsascode.Step;

import freemarker.ext.beans.BeanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.StringUtil;

public class UserPartOfStep implements TemplateMethodModelEx {
  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() != 1) {
      throw new TemplateModelException("Wrong number of arguments. Must be 1.");
    }

    String userPartOfStep = "";
    Step step = getStep(arguments.get(0));
    if (hasUser(step)) {
      String actors = getCapitalizedJoinedActors(step, "/");
      String wordsOfUserEventClassName = Words.getLowerCaseWordsOfClassName(step.getUserEventClass().getSimpleName());
      userPartOfStep = actors + " " + wordsOfUserEventClassName + ".";
    }
    
    return new SimpleScalar(userPartOfStep);
  }

  private boolean hasUser(Step step) {
    return step.getUseCaseModel().getSystemActor().getName() != step.getActors()[0].getName();
  }

  private String getCapitalizedJoinedActors(Step step, String separator) {
    String actorsLowerCase = StringUtils.join(step.getActors(), separator);
    String actorsUpperCase = StringUtil.capitalize(actorsLowerCase);
    return actorsUpperCase;
  }

  private Step getStep(Object argument) {
    return (Step) ((BeanModel) argument).getAdaptedObject(Step.class);
  }
}
