package org.requirementsascode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class RecordingActor extends AbstractActor {
  private static final Class<?> SYSTEM_EVENT_CLASS = ModelRunner.class;
  
  private final AbstractActor baseActor;
  private List<String> recordedStepNames;
  private List<Object> recordedMessages;

  private RecordingActor(AbstractActor baseActor) {
    this.baseActor = Objects.requireNonNull(baseActor, "baseActor must be non-null!");
    startRecordingStepsAndMessages();
  }

  private void startRecordingStepsAndMessages() {
    Consumer<StepToBeRun> messageHandler = getModelRunner().getMessageHandler();
    getModelRunner().handleWith(stepToBeRun -> {
      recordStepNameAndMessage(stepToBeRun.getStepName(), stepToBeRun.getMessage().orElse(null));
      messageHandler.accept(stepToBeRun);
    });
    recordedStepNames = new ArrayList<>();
    recordedMessages = new ArrayList<>();
  }
  
  private void recordStepNameAndMessage(String stepName, Object message) {
    recordedStepNames.add(stepName);
    if (message != null && !isSystemEvent(message)) {
      recordedMessages.add(message);
    }
  }
  
  private <T> boolean isSystemEvent(T message) {
    return SYSTEM_EVENT_CLASS.equals(message.getClass());
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
    String[] stepNames = recordedStepNames.stream().toArray(String[]::new);
    return stepNames;
  }

  /**
   * Returns the messages that caused a step to run, since the creation of this
   * actor.
   * 
   * @return the messages
   */
  public Object[] getRecordedMessages() {
    Object[] messages = recordedMessages.toArray();
    return messages;
  }
}
