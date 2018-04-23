package helloworld;

import org.requirementsascode.Model;
import org.requirementsascode.ModelBuilder;
import org.requirementsascode.ModelRunner;

public class HelloWorld02 {	
	
	private int hoorayCounter = 0;
	
	public Model buildWith(ModelBuilder modelBuilder) {
		Model useCaseModel = 
			modelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(this::greetsUser)
					.step("S2").system(this::printsHooray)
						.reactWhile(this::lessThanThreeHooraysHaveBeenPrinted)
			.build();
		
		return useCaseModel;
	}

	private void greetsUser(ModelRunner runner) {
		System.out.println("Hello, User.");
	}
	
	private boolean lessThanThreeHooraysHaveBeenPrinted(ModelRunner runner) {
		return hoorayCounter < 3; 
	}
	
	private void printsHooray(ModelRunner runner) {
		System.out.println("Hip, hip, hooray!");
		hoorayCounter++;
	}
	
	public static void main(String[] args){		
		HelloWorld02 example = new HelloWorld02();
		example.start();
	}

	private void start() {
		Model useCaseModel = buildWith(Model.builder());
		new ModelRunner().run(useCaseModel);
	}
}