# cross-cutting concerns example - measure performance of system reactions
``` java
class CrossCuttingConcernsActor01 extends AbstractActor {
  public CrossCuttingConcernsActor01() {
    getModelRunner().handleWith(this::measuresPerformance);
  }

  @Override
  public Model behavior() {
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
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/crosscuttingconcerns/src/main/java/crosscuttingconcerns/CrossCuttingConcerns01.java).
