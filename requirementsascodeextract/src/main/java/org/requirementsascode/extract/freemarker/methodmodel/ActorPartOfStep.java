package org.requirementsascode.extract.freemarker.methodmodel;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.requirementsascode.Actor;
import org.requirementsascode.Step;

import freemarker.ext.beans.BeanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class ActorPartOfStep implements TemplateMethodModelEx {
  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() != 1) {
      throw new TemplateModelException("Wrong number of arguments. Must be 1.");
    }

    Step step = getStep(arguments.get(0));
    String actors = getJoinedActors(step, "/");

    return new SimpleScalar(actors);
  }

  private String getJoinedActors(Step step, String separator) {
    String actorNames = "";
    if (hasNonDefaultUser(step)) {
      actorNames = "As " + StringUtils.join(step.getActors(), separator) + ": ";
    }
    return actorNames;
  }

  private boolean hasNonDefaultUser(Step step) {
    return !getUserActor(step).equals(step.getActors()[0]) &&
        !getSystemActor(step).equals(step.getActors()[0]);
  }
  
  private Actor getSystemActor(Step step) {
    return step.getUseCaseModel().getSystemActor();
  }
  
  private Actor getUserActor(Step step) {
    return step.getUseCaseModel().getUserActor();
  }

  private Step getStep(Object argument) {
    return (Step) ((BeanModel) argument).getAdaptedObject(Step.class);
  }
}
