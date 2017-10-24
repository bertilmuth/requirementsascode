package crosscuttingconcerns;

import org.requirementsascode.SystemReactionTrigger;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

public class CrossCuttingConcerns01 {
	private int resultOfAddition;
	private double resultOfPower;
	
	public void start() {		
		UseCaseModelBuilder useCaseModelBuilder = UseCaseModelBuilder.newBuilder();
		UseCaseModel useCaseModel = 
			useCaseModelBuilder.useCase("Measure performance of simple mathematical operations")
				.basicFlow()
					.step("S1").system(this::addsTwoNumbers)
					.step("S2").system(this::calculatesTwoToThePowerOfAThousand)
					.step("S3").system(this::displaysResults)
			.build();
		
		UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
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

	private void addsTwoNumbers(UseCaseModelRunner runner) {
		resultOfAddition = 123456789 + 123456789;
	}
	
	private void calculatesTwoToThePowerOfAThousand(UseCaseModelRunner runner) {
		resultOfPower = Math.pow(2, 1000);
	}
	
	private void displaysResults(UseCaseModelRunner runner) {
		System.out.println("Result of addition: " + resultOfAddition);	
		System.out.println("Result of 2 to the power of a thousand = " + resultOfPower);	
	}

	public static void main(String[] args){
		new CrossCuttingConcerns01().start();
	}
}
