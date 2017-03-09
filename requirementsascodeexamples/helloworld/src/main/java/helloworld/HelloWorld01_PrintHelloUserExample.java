package helloworld;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

public class HelloWorld01_PrintHelloUserExample {	
	
	public void create(UseCaseModel useCaseModel) {
		useCaseModel.useCase("Get greeted")
			.basicFlow()
				.step("S1").system(greetUser());
	}

	private Runnable greetUser() {
		return () -> System.out.println("Hello, User.");
	}
	
	public static void main(String[] args){
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		UseCaseModel useCaseModel = useCaseRunner.useCaseModel();
		
		HelloWorld01_PrintHelloUserExample example = new HelloWorld01_PrintHelloUserExample();
		example.create(useCaseModel);
		
		useCaseRunner.run();
	}
}