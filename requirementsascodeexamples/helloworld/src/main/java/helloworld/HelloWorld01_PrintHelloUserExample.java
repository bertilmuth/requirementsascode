package helloworld;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

public class HelloWorld01_PrintHelloUserExample {	
	
	public void start() {
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		UseCaseModel useCaseModel = useCaseRunner.getUseCaseModel();
				
		useCaseModel.newUseCase("Get greeted")
			.basicFlow()
				.newStep("System greets user.")
					.system(() -> System.out.println("Hello, User."));
		
		useCaseRunner.run();
	}
	
	public static void main(String[] args){
		new HelloWorld01_PrintHelloUserExample().start();
	}
}