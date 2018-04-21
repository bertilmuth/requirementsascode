package org.requirementsascode.extract.freemarker.methodmodel;

import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.getStepFromFreemarker;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.getUserActor;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.hasSystemEvent;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.hasSystemUser;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Words.getLowerCaseWordsOfClassName;

import java.util.List;

import org.requirementsascode.Step;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class UserPartOfStep implements TemplateMethodModelEx {
  private static final String USER_POSTFIX = ".";

  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() != 1) {
      throw new TemplateModelException("Wrong number of arguments. Must be 1.");
    }

    String userPartOfStep = "";
    Step step = getStepFromFreemarker(arguments.get(0));
    if (hasUser(step)) {
      String userActorName = getUserActor(step).getName();
      String wordsOfUserEventClassName = getLowerCaseWordsOfClassName(step.getEventClass());
      userPartOfStep = userActorName + " " + wordsOfUserEventClassName + USER_POSTFIX;
    }
    
    return new SimpleScalar(userPartOfStep);
  }
  
  private boolean hasUser(Step step) {
    return !hasSystemUser(step) && !hasSystemEvent(step);
  }
}
