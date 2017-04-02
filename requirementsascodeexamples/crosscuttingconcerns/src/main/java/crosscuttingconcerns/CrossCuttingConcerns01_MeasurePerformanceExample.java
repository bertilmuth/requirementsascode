package crosscuttingconcerns;

import java.util.function.Consumer;

import org.requirementsascode.SystemReactionTrigger;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

public class CrossCuttingConcerns01_MeasurePerformanceExample {
	private int resultOfAddition;
	private double resultOfPower;
	
	public void start() {
		UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
		useCaseModelRunner.adaptSystemReaction(this::measurePerformance);
		
		UseCaseModelBuilder useCaseModelBuilder = UseCaseModelBuilder.newBuilder();
		UseCaseModel useCaseModel = 
			useCaseModelBuilder.useCase("Measure performance of simple mathematical operations")
				.basicFlow()
					.step("S1").system(addTwoNumbers())
					.step("S2").system(calculateTwoToThePowerOfAThousand())
					.step("S3").system(displayResults())
				.build();
		
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

	private Consumer<UseCaseModelRunner> addTwoNumbers() {
		return r -> {
			resultOfAddition = 123456789 + 123456789;
		};
	}
	
	private Consumer<UseCaseModelRunner> calculateTwoToThePowerOfAThousand() {
		return r -> {
			resultOfPower = Math.pow(2, 1000);
		};
	}
	
	private Consumer<UseCaseModelRunner> displayResults() {
		return r -> {
			System.out.println("Result of addition: " + resultOfAddition);	
			System.out.println("Result of 2 to the power of a thousand = " + resultOfPower);	
		};
	}

	public static void main(String[] args){
		new CrossCuttingConcerns01_MeasurePerformanceExample().start();
	}
}
