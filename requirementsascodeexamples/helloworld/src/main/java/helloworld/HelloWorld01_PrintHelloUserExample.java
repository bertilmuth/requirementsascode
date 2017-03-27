package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.builder.UseCaseModelBuilder;

public class HelloWorld01_PrintHelloUserExample {	
	
	public UseCaseModel buildWith(UseCaseModelBuilder useCaseModelBuilder) {
		UseCaseModel useCaseModel = 
			useCaseModelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(greetUser())
				.build();
		return useCaseModel;
	}

	private Consumer<UseCaseRunner> greetUser() {
		return r -> System.out.println("Hello, User.");
	}
	
	public static void main(String[] args){
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		
		HelloWorld01_PrintHelloUserExample example = new HelloWorld01_PrintHelloUserExample();
		UseCaseModel useCaseModel = example.buildWith(new UseCaseModelBuilder());
		
		useCaseRunner.run(useCaseModel);
	}
}