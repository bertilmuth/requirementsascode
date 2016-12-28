package helloworld;

import java.util.function.Predicate;

import org.requirementsascode.UseCaseRunner;

public class HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample {	
	
	int hoorayCount = 0;
	
	public void start() {
		UseCaseRunner useCaseRunner = new UseCaseRunner();
				
		useCaseRunner.useCaseModel().useCase("Get greeted")
			.basicFlow()
				.step("S1").system(greetUser())
				.step("S2").system(printHooray())
					.repeatWhile(thereAreLessThanThreeHoorays());
		
		useCaseRunner.run();
	}

	private Runnable greetUser() {
		return () -> System.out.println("Hello, User.");
	}
	
	private Predicate<UseCaseRunner> thereAreLessThanThreeHoorays() {
		return r -> ++hoorayCount < 3;
	}
	
	private Runnable printHooray() {
		return () -> System.out.println("Hip, hip, hooray!");
	}
	
	public static void main(String[] args){
		new HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample().start();
	}
}