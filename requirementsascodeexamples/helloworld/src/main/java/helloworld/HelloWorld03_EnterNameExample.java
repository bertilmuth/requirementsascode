package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.builder.UseCaseModelBuilder;

public class HelloWorld03_EnterNameExample extends AbstractHelloWorldExample{
	
	private static final Class<EnterText> ENTER_FIRST_NAME = EnterText.class;

	public UseCaseModel buildWith(UseCaseModelBuilder useCaseModelBuilder) {
		UseCaseModel useCaseModel = 
			useCaseModelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(promptUserToEnterFirstName())
					.step("S2").user(ENTER_FIRST_NAME).system(greetUserWithFirstName())
			.build();
		return useCaseModel;
	}
	
	private Consumer<UseCaseRunner> promptUserToEnterFirstName() {
		return r -> System.out.print("Please enter your first name: ");
	}
	
	private Consumer<EnterText> greetUserWithFirstName() {
		return enterText -> System.out.println("Hello, " + enterText.text + ".");
	}
	
	public static void main(String[] args){
		UseCaseRunner useCaseRunner = new UseCaseRunner();

		HelloWorld03_EnterNameExample example = new HelloWorld03_EnterNameExample();
		UseCaseModel useCaseModel = example.buildWith(UseCaseModelBuilder.ofNewModel());
		
		useCaseRunner.run(useCaseModel);
		useCaseRunner.reactTo(example.enterText());
	}
}
