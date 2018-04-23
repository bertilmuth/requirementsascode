package helloworld;

import org.requirementsascode.Model;
import org.requirementsascode.ModelBuilder;
import org.requirementsascode.ModelRunner;

public class HelloWorld01 {	
	
	public Model buildWith(ModelBuilder modelBuilder) {
		Model useCaseModel = 
			modelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(this::greetsUser)
			.build();
		return useCaseModel;
	}

	private void greetsUser(ModelRunner runner) {
		System.out.println("Hello, User.");
	}
	
	public static void main(String[] args){		
		HelloWorld01 example = new HelloWorld01();
		example.start();
	}

	private void start() {
		Model useCaseModel = buildWith(Model.builder());
		new ModelRunner().run(useCaseModel);
	}
}