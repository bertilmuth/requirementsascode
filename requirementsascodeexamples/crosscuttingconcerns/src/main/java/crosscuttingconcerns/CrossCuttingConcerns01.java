package crosscuttingconcerns;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.StepToBeRun;

public class CrossCuttingConcerns01 {
	public void start() {
		Model model = Model.builder()
			.user(RequestCalculating.class).system(this::calculate)
		.build();

		ModelRunner modelRunner = new ModelRunner();
		modelRunner.handleWith(this::measuresPerformance);
		modelRunner.run(model).reactTo(new RequestCalculating());
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

	public static void main(String[] args) {
		new CrossCuttingConcerns01().start();
	}

	private class RequestCalculating {
	}
}
