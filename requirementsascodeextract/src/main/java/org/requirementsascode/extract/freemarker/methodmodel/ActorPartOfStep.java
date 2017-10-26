package org.requirementsascode.extract.freemarker.methodmodel;

import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.getStepFromFreemarker;
import static org.requirementsascode.extract.freemarker.methodmodel.util.Steps.hasNonDefaultActor;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.requirementsascode.Step;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class ActorPartOfStep implements TemplateMethodModelEx {
  private static final String ACTOR_PREFIX = "As ";
  private static final String ACTOR_SEPARATOR = "/";
  private static final String ACTOR_POSTFIX = ": ";

  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() != 1) {
      throw new TemplateModelException("Wrong number of arguments. Must be 1.");
    }

    Step step = getStepFromFreemarker(arguments.get(0));
    String actors = getJoinedActors(step, ACTOR_SEPARATOR);

    return new SimpleScalar(actors);
  }

  private String getJoinedActors(Step step, String separator) {
    String actorNames = "";
    if (hasNonDefaultActor(step)) {
      actorNames = ACTOR_PREFIX + StringUtils.join(step.getActors(), separator) + ACTOR_POSTFIX;
    }
    return actorNames;
  }
}
