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
					.handle(EnterFirstName.class)
					.system(greetUser());
		
		useCaseRunner.run();
		useCaseRunner.reactTo(enterFirstNameEvent());
	}
	
	private static EnterFirstName enterFirstNameEvent() {
		String firstName = enterFirstNameInConsole();
		return new EnterFirstName(firstName);
	}

	private static String enterFirstNameInConsole() {
		System.out.print("Please enter your first name: ");
		Scanner scanner = new Scanner(System.in);
		String firstName = scanner.next();
		scanner.close();
		return firstName;
	}
	
	private static Consumer<EnterFirstName> greetUser() {
		return event -> System.out.println("Hello, " + event.getFirstName() + ".");
	}
	
	private static class EnterFirstName {
		private String firstName;
		public EnterFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getFirstName() {
			return firstName;
		}
	}
}
