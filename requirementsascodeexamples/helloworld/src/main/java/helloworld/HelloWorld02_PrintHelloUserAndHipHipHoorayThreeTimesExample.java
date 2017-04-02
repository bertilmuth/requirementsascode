package helloworld;

import static org.requirementsascode.builder.UseCaseModelBuilder.newBuilder;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelRunner;
import org.requirementsascode.builder.UseCaseModelBuilder;

public class HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample {	
	
	int hoorayCounter = 0;
	
	public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
		UseCaseModel useCaseModel = 
			modelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(greetUser())
					.step("S2").system(printHooray())
						.reactWhile(lessThanThreeHooraysHaveBeenPrinted())
			.build();
		
		return useCaseModel;
	}

	private Consumer<UseCaseModelRunner> greetUser() {
		return r -> System.out.println("Hello, User.");
	}
	
	private Predicate<UseCaseModelRunner> lessThanThreeHooraysHaveBeenPrinted() {
		return r -> hoorayCounter < 3; 
	}
	
	private Consumer<UseCaseModelRunner> printHooray() {
		return r -> {
			System.out.println("Hip, hip, hooray!");
			hoorayCounter++;
		};
	}
	
	public static void main(String[] args){		
		HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample example = new HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample();
		example.start();
	}

	private void start() {
		UseCaseModelRunner useCaseRunner = new UseCaseModelRunner();
		UseCaseModel useCaseModel = buildWith(newBuilder());
		useCaseRunner.run(useCaseModel);
	}
}