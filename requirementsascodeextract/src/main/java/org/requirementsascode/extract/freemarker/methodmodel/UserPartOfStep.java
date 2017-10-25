package org.requirementsascode.extract.freemarker.methodmodel;

import java.util.List;

import org.requirementsascode.Step;
import org.requirementsascode.UseCaseModelRunner;

import freemarker.ext.beans.BeanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

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
      String userActorName = getUserActorName(step);
      String wordsOfUserEventClassName = Words.getLowerCaseWordsOfClassName(getUserEventName(step));
      userPartOfStep = userActorName + " " + wordsOfUserEventClassName + ".";
    }
    
    return new SimpleScalar(userPartOfStep);
  }
  
  private String getUserEventName(Step step) {
    Class<?> userEvent = step.getUserEventClass();
    String userEventName = userEvent.getSimpleName();
    return userEventName;
  }
  
  private boolean hasUser(Step step) {
    return !UseCaseModelRunner.class.equals(step.getUserEventClass());
  }
  
  private String getUserActorName(Step step) {
    return step.getUseCaseModel().getUserActor().getName();
  }

  private Step getStep(Object argument) {
    return (Step) ((BeanModel) argument).getAdaptedObject(Step.class);
  }
}
