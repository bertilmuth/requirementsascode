package crosscuttingconcerns;

import org.requirementsascode.AbstractActor;
import org.requirementsascode.Model;
import org.requirementsascode.StepToBeRun;

public class CrossCuttingConcerns01 {
  public static void main(String[] args) {
    new CrossCuttingConcernsActor01().reactTo(new RequestCalculating());
  }
}

class CrossCuttingConcernsActor01 extends AbstractActor {
  public CrossCuttingConcernsActor01() {
    getModelRunner().handleWith(this::measuresPerformance);
  }

  @Override
  protected Model behavior() {
    Model model = Model.builder()
      .user(RequestCalculating.class).system(this::calculate)
      .build();
    return model;
  }

  private void measuresPerformance(StepToBeRun stepToBeRun) {
    long timeBefore = System.nanoTime();
    stepToBeRun.run();
    long timeAfter = System.nanoTime();
    long timeElapsed = timeAfter - timeBefore;

    System.out.println("Elapsed time: " + timeElapsed + " nanoseconds.");
  }

  private void calculate() {
    Math.pow(2, 1000);
  }
}

class RequestCalculating {
}
