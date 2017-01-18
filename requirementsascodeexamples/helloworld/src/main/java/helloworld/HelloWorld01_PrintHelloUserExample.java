package helloworld;

import org.requirementsascode.UseCaseRunner;

public class HelloWorld01_PrintHelloUserExample {	
	
	public void createModelFor(UseCaseRunner useCaseRunner) {
		useCaseRunner.useCaseModel().useCase("Get greeted")
			.basicFlow()
				.step("S1").system(greetUser());
	}

	private Runnable greetUser() {
		return () -> System.out.println("Hello, User.");
	}
	
	public static void main(String[] args){
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		HelloWorld01_PrintHelloUserExample example = new HelloWorld01_PrintHelloUserExample();
		example.createModelFor(useCaseRunner);
		
		useCaseRunner.run();
	}
}