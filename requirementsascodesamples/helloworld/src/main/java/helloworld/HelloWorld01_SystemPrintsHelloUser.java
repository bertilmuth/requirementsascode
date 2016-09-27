package helloworld;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelRun;

public class HelloWorld01_SystemPrintsHelloUser {
	public static void main(String[] args) {
		UseCaseModelRun useCaseModelRun = new UseCaseModelRun();
		UseCaseModel useCaseModel = useCaseModelRun.getModel();
		
		Actor user = useCaseModel.newActor("User");
		
		useCaseModel.newUseCase("Get greeted")
			.basicFlow()
				.newStep("System greets user.")
					.system(() -> System.out.println("Hello, User."));
		
		useCaseModelRun.as(user);
	}
}
