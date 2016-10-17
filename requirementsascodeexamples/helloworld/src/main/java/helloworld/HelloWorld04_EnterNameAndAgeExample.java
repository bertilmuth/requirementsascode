package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

public class HelloWorld04_EnterNameAndAgeExample extends AbstractHelloWorldExample{
	private String firstName;
	private int age;
	
	public void start() {	
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		UseCaseModel useCaseModel = useCaseRunner.getUseCaseModel();
				
		useCaseModel.newUseCase("Get greeted")
			.basicFlow()
				.newStep("System prompts user to enter first name.")
					.system(promptUserToEnterFirstName())
				.newStep("User enters first name. System saves the first name.")
					.handle(EnterTextEvent.class).system(saveFirstName())
				.newStep("System prompts user to enter age.")
					.system(promptUserToEnterAge())
				.newStep("User enters age. System saves age.")
					.handle(EnterTextEvent.class).system(saveAge())
				.newStep("System greets user with first name and age.")
					.system(greetUserWithFirstNameAndAge())
				.newStep("System terminates application.")
					.system(terminateApplication());
		
		useCaseRunner.run();
		
		useCaseRunner.reactTo(enterTextEvent());
		useCaseRunner.reactTo(enterTextEvent());
	}
	
	private Runnable promptUserToEnterFirstName() {
		return () -> System.out.print("Please enter your first name: ");
	}
	
	private Runnable promptUserToEnterAge() {
		return () -> System.out.print("Please enter your age: ");
	}

	private Consumer<EnterTextEvent> saveFirstName() {
		return enterTextEvent -> firstName = enterTextEvent.getText();
	}
	
	private Consumer<EnterTextEvent> saveAge() {
		return enterTextEvent -> age = Integer.parseInt(enterTextEvent.getText());
	}
	
	private Runnable greetUserWithFirstNameAndAge() {
		return () -> System.out.println("Hello, " + 
			firstName + " (" + age + ").");
	}
	
	public static void main(String[] args){
		new HelloWorld04_EnterNameAndAgeExample().start();
	}
}
