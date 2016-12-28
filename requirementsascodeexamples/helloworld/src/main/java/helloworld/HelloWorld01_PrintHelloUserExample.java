package helloworld;

import org.requirementsascode.UseCaseRunner;

public class HelloWorld01_PrintHelloUserExample {	
	
	public void start() {
		UseCaseRunner useCaseRunner = new UseCaseRunner();
				
		useCaseRunner.useCaseModel().useCase("Get greeted")
			.basicFlow()
				.step("S1").system(greetUser());
		
		useCaseRunner.run();
	}

	private Runnable greetUser() {
		return () -> System.out.println("Hello, User.");
	}
	
	public static void main(String[] args){
		new HelloWorld01_PrintHelloUserExample().start();
	}
}