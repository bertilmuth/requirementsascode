package helloworld;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

public class HelloWorld02 {	
	
	private int hoorayCounter = 0;
	
	public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
		UseCaseModel useCaseModel = 
			modelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(this::greetUser)
					.step("S2").system(this::printHooray)
						.reactWhile(this::lessThanThreeHooraysHaveBeenPrinted)
			.build();
		
		return useCaseModel;
	}

	private void greetUser(UseCaseModelRunner runner) {
		System.out.println("Hello, User.");
	}
	
	private boolean lessThanThreeHooraysHaveBeenPrinted(UseCaseModelRunner runner) {
		return hoorayCounter < 3; 
	}
	
	private void printHooray(UseCaseModelRunner runner) {
		System.out.println("Hip, hip, hooray!");
		hoorayCounter++;
	}
	
	public static void main(String[] args){		
		HelloWorld02 example = new HelloWorld02();
		example.start();
	}

	private void start() {
		UseCaseModel useCaseModel = buildWith(UseCaseModelBuilder.newBuilder());
		new UseCaseModelRunner().run(useCaseModel);
	}
}