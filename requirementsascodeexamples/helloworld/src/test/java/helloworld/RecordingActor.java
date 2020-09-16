package helloworld;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;

public class RecordingActor extends AbstractActor {
  private AbstractActor baseActor;

  private RecordingActor(AbstractActor baseActor) {
    this.baseActor = baseActor;
    super.getModelRunner().startRecording();
  }

  public static RecordingActor basedOn(AbstractActor baseActor) {
    return new RecordingActor(baseActor);
  }

  @Override
  public Model behavior() {
    return baseActor.behavior();
  }

  public String[] getRecordedStepNames() {
    String[] recordedStepNames = super.getModelRunner().getRecordedStepNames();
    return recordedStepNames;
  }
}
