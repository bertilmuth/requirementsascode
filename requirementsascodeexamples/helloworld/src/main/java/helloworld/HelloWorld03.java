package helloworld;

import static org.requirementsascode.UseCaseModelBuilder.newBuilder;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

import helloworld.userevent.EnterText;

public class HelloWorld03 extends AbstractHelloWorldExample{
	
	private static final Class<EnterText> ENTER_FIRST_NAME = EnterText.class;

	public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
		UseCaseModel useCaseModel = 
			modelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(this::promptUserToEnterFirstName)
					.step("S2").user(ENTER_FIRST_NAME).system(this::greetUserWithFirstName)
			.build();
		return useCaseModel;
	}
	
	private void promptUserToEnterFirstName(UseCaseModelRunner runner) {
		System.out.print("Please enter your first name: ");
	}
	
	private void greetUserWithFirstName(EnterText enterText) {
		System.out.println("Hello, " + enterText.text + ".");
	}
	
	public static void main(String[] args){		
		HelloWorld03 example = new HelloWorld03();
		example.start();
	}

	private void start() {
		UseCaseModel useCaseModel = buildWith(newBuilder());
		UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
		useCaseModelRunner.run(useCaseModel);
		useCaseModelRunner.reactTo(enterText());
	}
}
