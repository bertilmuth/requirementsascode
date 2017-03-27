package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

public class HelloWorld01_PrintHelloUserExample {	
	
	public void create(UseCaseModel useCaseModel) {
		useCaseModel.useCase("Get greeted")
			.basicFlow()
				.step("S1").system(greetUser());
	}

	private Consumer<UseCaseRunner> greetUser() {
		return r -> System.out.println("Hello, User.");
	}
	
	public static void main(String[] args){
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		UseCaseModel useCaseModel = useCaseRunner.useCaseModel();
		
		HelloWorld01_PrintHelloUserExample example = new HelloWorld01_PrintHelloUserExample();
		example.create(useCaseModel);
		
		useCaseRunner.run();
	}
}