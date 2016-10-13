package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

public class HelloWorld02_EnterNameExample extends AbstractHelloWorldExample{
	
	public void start() {
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		UseCaseModel useCaseModel = useCaseRunner.getUseCaseModel();
				
		useCaseModel.newUseCase("Get greeted")
			.basicFlow()
				.newStep("System prompts user to enter first name")
					.system(promptUserToEnterFirstName())
				.newStep("User enters first name. System greets user with first name.")
					.handle(EnterTextEvent.class).system(greetUserWithFirstName())
				.newStep("Application terminates")
					.system(terminateApplication());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterTextEvent());
	}
	
	private Runnable promptUserToEnterFirstName() {
		return () -> System.out.print("Please enter your first name: ");
	}
	
	private Consumer<EnterTextEvent> greetUserWithFirstName() {
		return enterTextEvent -> System.out.println("Hello, " + 
			enterTextEvent.getText() + ".");
	}
	
	public static void main(String[] args){
		new HelloWorld02_EnterNameExample().start();
	}
}
