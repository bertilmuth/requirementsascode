package org.requirementsascode;

/**
 * Simple use case model runner for automated tests.
 *
 * @author b_muth
 */
public class TestUseCaseModelRunner extends UseCaseModelRunner {
  private StringBuilder runStepNames;

  public TestUseCaseModelRunner() {
    runStepNames = new StringBuilder();
    adaptSystemReaction(this::withStepNameTracking);
  }

  /**
   * Returns the names of the use case steps that have been run, separated with a semicolon after
   * each step, without spaces.
   *
   * <p>If no step has been run, an empty string is returned. If one step S1 has been run, the
   * string S1; is returned. If two steps, S1 and S2 have been run, the string S1;S2; is returned.
   *
   * @return the step names
   */
  public String getRunStepNames() {
    return runStepNames.toString();
  }

  private void withStepNameTracking(SystemReactionTrigger systemReactionTrigger) {
    String stepName = systemReactionTrigger.getUseCaseStep().getName();
    runStepNames.append(stepName);
    runStepNames.append(";");
    systemReactionTrigger.trigger();
  }
}
