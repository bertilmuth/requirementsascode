package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

public class HelloWorld03_UserEntersNameAndAgeExample extends AbstractHelloWorldExample{
	private String firstName;
	private String age;
	
	public void start() {	
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		UseCaseModel useCaseModel = useCaseRunner.getUseCaseModel();
				
		useCaseModel.newUseCase("Get greeted")
			.basicFlow()
				.newStep("User enters first name. System saves the first name.")
					.handle(EnterTextEvent.class).system(saveName())
				.newStep("User enters age. System saves age.")
					.handle(EnterTextEvent.class).system(saveAge())
				.newStep("System greets user with first name and age.")
					.system(greetUserWithFirstNameAndAge());
		
		useCaseRunner.run();
		
		String firstName = enterText("Please enter your first name: ");
		useCaseRunner.reactTo(new EnterTextEvent(firstName));
		
		String age = enterText("Please enter your age: ");
		useCaseRunner.reactTo(new EnterTextEvent(age));
		
		theEnd();
	}

	private Consumer<EnterTextEvent> saveName() {
		return enterTextEvent -> firstName = enterTextEvent.getText();
	}
	
	private Consumer<EnterTextEvent> saveAge() {
		return enterTextEvent -> age = enterTextEvent.getText();
	}
	
	private Runnable greetUserWithFirstNameAndAge() {
		return () -> System.out.println("Hello, " + 
			firstName + " (" + age + ").");
	}
	
	public static void main(String[] args){
		new HelloWorld03_UserEntersNameAndAgeExample().start();
	}
}
