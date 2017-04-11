package helloworld;

import static org.requirementsascode.UseCaseModelBuilder.newBuilder;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

public class HelloWorld01 {	
	
	public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
		UseCaseModel useCaseModel = 
			modelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(this::greetUser)
			.build();
		return useCaseModel;
	}

	private void greetUser(UseCaseModelRunner runner) {
		System.out.println("Hello, User.");
	}
	
	public static void main(String[] args){		
		HelloWorld01 example = new HelloWorld01();
		example.start();
	}

	private void start() {
		UseCaseModel useCaseModel = buildWith(newBuilder());
		new UseCaseModelRunner().run(useCaseModel);
	}
}