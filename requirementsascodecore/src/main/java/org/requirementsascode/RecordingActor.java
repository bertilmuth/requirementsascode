package org.requirementsascode;

import java.util.Objects;

public class RecordingActor extends AbstractActor {
  private AbstractActor baseActor;

  private RecordingActor(AbstractActor baseActor) {
    this.baseActor = Objects.requireNonNull(baseActor);
    baseActor.getModelRunner().startRecording();
  }

  /**
   * Creates an actor that records the steps and messages that are handled
   * by the specified base actor.
   * 
   * @param baseActor the actor whose steps and messages to record
   * @return an actor ready for recording
   */
  public static RecordingActor basedOn(AbstractActor baseActor) {
    return new RecordingActor(baseActor);
  }

  @Override
  public ModelRunner getModelRunner() {
    return baseActor.getModelRunner();
  }

  @Override
  public Model behavior() {
    return baseActor.behavior();
  }

  /**
   * Returns the names of steps that have been run, since the creation of this
   * actor.
   * 
   * @return the step names
   */
  public String[] getRecordedStepNames() {
    String[] recordedStepNames = getModelRunner().getRecordedStepNames();
    return recordedStepNames;
  }

  /**
   * Returns the messages that caused a step to run, since the creation of this
   * actor.
   * 
   * @return the messages
   */
  public Object[] getRecordedMessages() {
    Object[] recordedMessages = getModelRunner().getRecordedMessages();
    return recordedMessages;
  }
}
