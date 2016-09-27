package helloworld;

import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelRun;

public class HelloWorld {
	public static void main(String[] args) {
		UseCaseModelRun useCaseModelRun = new UseCaseModelRun();
		UseCaseModel useCaseModel = useCaseModelRun.getModel();
		
		Actor user = useCaseModel.newActor("User");
		
		useCaseModel.newUseCase("Get greeted")
			.basicFlow()
				.newStep("User gets greeted by the system.")
					.system(() -> System.out.println("Hello, user."));
		
		useCaseModelRun.as(user);
	}
}
