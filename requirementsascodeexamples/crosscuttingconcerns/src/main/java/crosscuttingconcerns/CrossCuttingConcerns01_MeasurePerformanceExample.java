package crosscuttingconcerns;

import java.util.function.Consumer;

import org.requirementsascode.SystemReactionTrigger;
import org.requirementsascode.UseCaseRunner;

public class CrossCuttingConcerns01_MeasurePerformanceExample {
	private int resultOfAddition;
	private double resultOfPower;
	
	public void start() {
		UseCaseRunner useCaseRunner = new UseCaseRunner(withMeasuringPerformance());
		
		useCaseRunner.useCaseModel().useCase("Measure performance of simple mathematical operations")
			.basicFlow()
				.step("S1").system(addTwoNumbers())
				.step("S2").system(calculateTwoToThePowerOfAThousand())
				.step("S3").system(displayResults());
		
		useCaseRunner.run();
	}

	private Consumer<SystemReactionTrigger> withMeasuringPerformance() {
		return systemReactionTrigger -> {
			long timeBefore = System.nanoTime();
			systemReactionTrigger.trigger();
			long timeafter = System.nanoTime();
			long timeElapsed = timeafter - timeBefore;
			
			System.out.println("Step " + systemReactionTrigger.useCaseStep() + 
				" took " + timeElapsed + " nanoseconds.");
		};
	}

	private Runnable addTwoNumbers() {
		return () -> {
			resultOfAddition = 123456789 + 123456789;
		};
	}
	
	private Runnable calculateTwoToThePowerOfAThousand() {
		return () -> {
			resultOfPower = Math.pow(2, 1000);
		};
	}
	
	private Runnable displayResults() {
		return () ->{
			System.out.println("Result of addition: " + resultOfAddition);	
			System.out.println("Result of 2 to the power of a thousand = " + resultOfPower);	
		};
	}

	public static void main(String[] args){
		new CrossCuttingConcerns01_MeasurePerformanceExample().start();
	}
}
