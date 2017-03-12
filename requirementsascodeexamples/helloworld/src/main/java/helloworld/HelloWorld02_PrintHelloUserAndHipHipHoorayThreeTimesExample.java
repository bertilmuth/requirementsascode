package helloworld;

import java.util.function.Predicate;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

public class HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample {	
	
	int hoorayCounter = 0;
	
	public void create(UseCaseModel useCaseModel) {
		useCaseModel.useCase("Get greeted")
			.basicFlow()
				.step("S1").system(greetUser())
				.step("S2").system(printHooray())
					.reactWhile(lessThanThreeHooraysHaveBeenPrinted());
		
	}

	private Runnable greetUser() {
		return () -> System.out.println("Hello, User.");
	}
	
	private Predicate<UseCaseRunner> lessThanThreeHooraysHaveBeenPrinted() {
		return r -> hoorayCounter < 3; 
	}
	
	private Runnable printHooray() {
		return () -> {
			System.out.println("Hip, hip, hooray!");
			hoorayCounter++;
		};
	}
	
	public static void main(String[] args){
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		UseCaseModel useCaseModel = useCaseRunner.useCaseModel();

		HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample example = new HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample();
		example.create(useCaseModel);
		
		useCaseRunner.run();
	}
}