package org.requirementsascode;

public class RecordingActor extends AbstractActor {
  private AbstractActor baseActor;

  private RecordingActor(AbstractActor baseActor) {
    this.baseActor = baseActor;
    baseActor.getModelRunner().startRecording();
  }

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

  public String[] getRecordedStepNames() {
    String[] recordedStepNames = getModelRunner().getRecordedStepNames();
    return recordedStepNames;
  }
}
