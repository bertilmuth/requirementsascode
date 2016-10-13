package helloworld;

import java.util.Scanner;
import java.util.function.Consumer;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

public class HelloWorld02_UserEntersName {
	public static void main(String[] args) {
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		UseCaseModel useCaseModel = useCaseRunner.getUseCaseModel();
				
		useCaseModel.newUseCase("Get greeted")
			.basicFlow()
				.newStep("User enters first name. System greets user with first name.")
					.handle(EnterFirstNameEvent.class)
					.system(greetUser());
		
		useCaseRunner.run();
		
		String firstName = enterFirstNameInConsole();
		useCaseRunner.reactTo(new EnterFirstNameEvent(firstName));
	}

	private static String enterFirstNameInConsole() {
		System.out.print("Please enter your first name: ");
		Scanner scanner = new Scanner(System.in);
		String firstName = scanner.next();
		scanner.close();
		return firstName;
	}
	
	private static Consumer<EnterFirstNameEvent> greetUser() {
		return enterFirstNameEvent -> System.out.println("Hello, " + 
			enterFirstNameEvent.getFirstName() + ".");
	}
	
	private static class EnterFirstNameEvent {
		private String firstName;
		public EnterFirstNameEvent(String firstName) {
			this.firstName = firstName;
		}
		public String getFirstName() {
			return firstName;
		}
	}
}
