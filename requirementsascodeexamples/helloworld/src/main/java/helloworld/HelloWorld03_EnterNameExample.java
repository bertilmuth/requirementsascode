package helloworld;

import static org.requirementsascode.UseCaseModelBuilder.newBuilder;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

public class HelloWorld03_EnterNameExample extends AbstractHelloWorldExample{
	
	private static final Class<EnterText> ENTER_FIRST_NAME = EnterText.class;

	public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
		UseCaseModel useCaseModel = 
			modelBuilder.useCase("Get greeted")
				.basicFlow()
					.step("S1").system(promptUserToEnterFirstName())
					.step("S2").user(ENTER_FIRST_NAME).system(greetUserWithFirstName())
			.build();
		return useCaseModel;
	}
	
	private Consumer<UseCaseModelRunner> promptUserToEnterFirstName() {
		return r -> System.out.print("Please enter your first name: ");
	}
	
	private Consumer<EnterText> greetUserWithFirstName() {
		return enterText -> System.out.println("Hello, " + enterText.text + ".");
	}
	
	public static void main(String[] args){		
		HelloWorld03_EnterNameExample example = new HelloWorld03_EnterNameExample();
		example.start();
	}

	private void start() {
		UseCaseModelRunner useCaseRunner = new UseCaseModelRunner();
		UseCaseModel useCaseModel = buildWith(newBuilder());
		useCaseRunner.run(useCaseModel);
		useCaseRunner.reactTo(enterText());
	}
}
