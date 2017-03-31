package helloworld;

import static org.requirementsascode.builder.UseCaseModelBuilder.newModelBuilder;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;
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

	private Consumer<UseCaseRunner> greetUser() {
		return r -> System.out.println("Hello, User.");
	}
	
	private Predicate<UseCaseRunner> lessThanThreeHooraysHaveBeenPrinted() {
		return r -> hoorayCounter < 3; 
	}
	
	private Consumer<UseCaseRunner> printHooray() {
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
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		UseCaseModel useCaseModel = buildWith(newModelBuilder());
		useCaseRunner.run(useCaseModel);
	}
}