package helloworld;

import static org.requirementsascode.builder.UseCaseModelBuilder.newBuilder;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelRunner;
import org.requirementsascode.builder.UseCaseModelBuilder;

public class HelloWorld01_PrintHelloUserExample {	
	
	public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
		UseCaseModel useCaseModel = 
			modelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(greetUser())
				.build();
		return useCaseModel;
	}

	private Consumer<UseCaseModelRunner> greetUser() {
		return r -> System.out.println("Hello, User.");
	}
	
	public static void main(String[] args){		
		HelloWorld01_PrintHelloUserExample example = new HelloWorld01_PrintHelloUserExample();
		example.start();
	}

	private void start() {
		UseCaseModelRunner useCaseRunner = new UseCaseModelRunner();
		UseCaseModel useCaseModel = buildWith(newBuilder());
		useCaseRunner.run(useCaseModel);
	}
}