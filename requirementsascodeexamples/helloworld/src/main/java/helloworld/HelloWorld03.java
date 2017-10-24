package helloworld;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

import helloworld.userevent.EntersText;

public class HelloWorld03 extends AbstractHelloWorldExample{
	
	private static final Class<EntersText> ENTER_FIRST_NAME = EntersText.class;

	public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
		UseCaseModel useCaseModel = 
			modelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(this::promptsUserToEnterFirstName)
					.step("S2").user(ENTER_FIRST_NAME).system(this::greetsUserWithFirstName)
			.build();
		return useCaseModel;
	}
	
	private void promptsUserToEnterFirstName(UseCaseModelRunner runner) {
		System.out.print("Please enter your first name: ");
	}
	
	private void greetsUserWithFirstName(EntersText enterText) {
		System.out.println("Hello, " + enterText.text + ".");
	}
	
	public static void main(String[] args){		
		HelloWorld03 example = new HelloWorld03();
		example.start();
	}

	private void start() {
		UseCaseModel useCaseModel = buildWith(UseCaseModelBuilder.newBuilder());
		UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(entersText());
	}
}
