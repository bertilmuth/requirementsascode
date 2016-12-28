package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseRunner;

public class HelloWorld03_EnterNameExample extends AbstractHelloWorldExample{
	
	private static final Class<EnterText> ENTER_FIRST_NAME = EnterText.class;

	public void start() {
		UseCaseRunner useCaseRunner = new UseCaseRunner();
				
		useCaseRunner.useCaseModel().useCase("Get greeted")
			.basicFlow()
				.step("S1").system(promptUserToEnterFirstName())
				.step("S2").user(ENTER_FIRST_NAME).system(greetUserWithFirstName())
				.step("S3").system(terminateApplication());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterText());
	}
	
	private Runnable promptUserToEnterFirstName() {
		return () -> System.out.print("Please enter your first name: ");
	}
	
	private Consumer<EnterText> greetUserWithFirstName() {
		return enterText -> System.out.println("Hello, " + enterText.text + ".");
	}
	
	public static void main(String[] args){
		new HelloWorld03_EnterNameExample().start();
	}
}
