package crosscuttingconcerns;

import org.requirementsascode.SystemReactionTrigger;
import org.requirementsascode.Model;
import org.requirementsascode.ModelBuilder;
import org.requirementsascode.ModelRunner;

public class CrossCuttingConcerns01 {
	private int resultOfAddition;
	private double resultOfPower;
	
	public void start() {		
		ModelBuilder useCaseModelBuilder = Model.builder();
		Model useCaseModel = 
			useCaseModelBuilder.useCase("Measure performance of simple mathematical operations")
				.basicFlow()
					.step("S1").system(this::addsTwoNumbers)
					.step("S2").system(this::calculatesTwoToThePowerOfAThousand)
					.step("S3").system(this::displaysResults)
			.build();
		
		ModelRunner useCaseModelRunner = new ModelRunner();
		useCaseModelRunner.adaptSystemReaction(this::measuresPerformance);
		useCaseModelRunner.run(useCaseModel);
	}
	
	private void measuresPerformance(SystemReactionTrigger systemReactionTrigger) {
			long timeBefore = System.nanoTime();
			systemReactionTrigger.trigger();
			long timeAfter = System.nanoTime();
			long timeElapsed = timeAfter - timeBefore;
			
			System.out.println("Step " + systemReactionTrigger.getUseCaseStep() + 
				" took " + timeElapsed + " nanoseconds.");
	}

	private void addsTwoNumbers(ModelRunner runner) {
		resultOfAddition = 123456789 + 123456789;
	}
	
	private void calculatesTwoToThePowerOfAThousand(ModelRunner runner) {
		resultOfPower = Math.pow(2, 1000);
	}
	
	private void displaysResults(ModelRunner runner) {
		System.out.println("Result of addition: " + resultOfAddition);	
		System.out.println("Result of 2 to the power of a thousand = " + resultOfPower);	
	}

	public static void main(String[] args){
		new CrossCuttingConcerns01().start();
	}
}
