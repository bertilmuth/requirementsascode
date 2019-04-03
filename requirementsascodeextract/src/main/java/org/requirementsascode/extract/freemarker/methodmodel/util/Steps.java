package org.requirementsascode.extract.freemarker.methodmodel.util;

import org.requirementsascode.Actor;
import org.requirementsascode.Flow;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.Step;
import org.requirementsascode.systemreaction.IgnoresIt;

import freemarker.ext.beans.BeanModel;

public class Steps {
  public static Class<?> getClassFromFreemarker(Object argument) {
    return (Class<?>) ((BeanModel) argument).getAdaptedObject(Class.class);
  }
  
  public static Step getStepFromFreemarker(Object argument) {
    return (Step) ((BeanModel) argument).getAdaptedObject(Step.class);
  }
  
  public static Flow getFlowFromFreemarker(Object argument) {
    return (Flow) ((BeanModel) argument).getAdaptedObject(Flow.class);
  }
  
  public static boolean hasNonDefaultActor(Step step) {
    Actor firstActorOfStep = getFirstActorOfStep(step);
    return !getUserActor(step).equals(firstActorOfStep) && !getSystemActor(step).equals(firstActorOfStep);
  }
  
  public static boolean hasSystemUser(Step step) {
    return getSystemActor(step).equals(getFirstActorOfStep(step));
  }
  
  public static boolean hasSystemEvent(Step step) {
    return ModelRunner.class.equals(step.getEventClass());
  }

  public static boolean hasSystemReaction(Step step) {
    return !(step.getSystemReaction().getModelObject() instanceof IgnoresIt<?>);
  }
  
  public static Actor getUserActor(Step step) {
    return step.getModel().getUserActor();
  }
  
  public static Actor getSystemActor(Step step) {
    return step.getModel().getSystemActor();
  }
  
  private static Actor getFirstActorOfStep(Step step) {
    return step.getActors()[0];
  }
}
