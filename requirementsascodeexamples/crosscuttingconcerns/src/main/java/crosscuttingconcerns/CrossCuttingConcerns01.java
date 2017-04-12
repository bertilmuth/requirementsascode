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
					.step("S1").system(this::addTwoNumbers)
					.step("S2").system(this::calculateTwoToThePowerOfAThousand)
					.step("S3").system(this::displayResults)
			.build();
		
		UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
		useCaseModelRunner.adaptSystemReaction(this::measurePerformance);
		useCaseModelRunner.run(useCaseModel);
	}
	
	private void measurePerformance(SystemReactionTrigger systemReactionTrigger) {
			long timeBefore = System.nanoTime();
			systemReactionTrigger.trigger();
			long timeafter = System.nanoTime();
			long timeElapsed = timeafter - timeBefore;
			
			System.out.println("Step " + systemReactionTrigger.getUseCaseStep() + 
				" took " + timeElapsed + " nanoseconds.");
	}

	private void addTwoNumbers(UseCaseModelRunner runner) {
		resultOfAddition = 123456789 + 123456789;
	}
	
	private void calculateTwoToThePowerOfAThousand(UseCaseModelRunner runner) {
		resultOfPower = Math.pow(2, 1000);
	}
	
	private void displayResults(UseCaseModelRunner runner) {
		System.out.println("Result of addition: " + resultOfAddition);	
		System.out.println("Result of 2 to the power of a thousand = " + resultOfPower);	
	}

	public static void main(String[] args){
		new CrossCuttingConcerns01().start();
	}
}
