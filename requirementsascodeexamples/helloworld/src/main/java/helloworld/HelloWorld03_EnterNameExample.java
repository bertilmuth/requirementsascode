package helloworld;

import static org.requirementsascode.builder.UseCaseModelBuilder.newModelBuilder;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.builder.UseCaseModelBuilder;

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
	
	private Consumer<UseCaseRunner> promptUserToEnterFirstName() {
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
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		UseCaseModel useCaseModel = buildWith(newModelBuilder());
		useCaseRunner.run(useCaseModel);
		useCaseRunner.reactTo(enterText());
	}
}
