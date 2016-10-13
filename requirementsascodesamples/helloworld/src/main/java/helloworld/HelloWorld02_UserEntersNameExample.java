package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

public class HelloWorld02_UserEntersNameExample extends AbstractHelloWorldExample{
	
	public void start() {
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		UseCaseModel useCaseModel = useCaseRunner.getUseCaseModel();
				
		useCaseModel.newUseCase("Get greeted")
			.basicFlow()
				.newStep("User enters first name. System greets user with first name.")
					.handle(EnterTextEvent.class).system(greetUserWithFirstName());
		
		useCaseRunner.run();
		
		String firstName = enterText("Please enter your first name: ");
		useCaseRunner.reactTo(new EnterTextEvent(firstName));
		
		theEnd();
	}
	
	private Consumer<EnterTextEvent> greetUserWithFirstName() {
		return enterTextEvent -> System.out.println("Hello, " + 
			enterTextEvent.getText() + ".");
	}
	
	public static void main(String[] args){
		new HelloWorld02_UserEntersNameExample().start();
	}
}
