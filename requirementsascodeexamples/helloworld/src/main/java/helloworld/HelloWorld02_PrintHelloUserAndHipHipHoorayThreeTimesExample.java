package helloworld;

import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseRunner;

public class HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample {	
	
	int hoorayCount = 0;
	
	public void start() {
		UseCaseRunner useCaseRunner = new UseCaseRunner();
		UseCaseModel useCaseModel = useCaseRunner.getUseCaseModel();
				
		useCaseModel.newUseCase("Get greeted")
			.basicFlow()
				.newStep("System greets user.")
					.system(() -> System.out.println("Hello, User."))
				.newStep("System prints 'Hip, hip, hooray!' three times.")
					.system(() -> System.out.println("Hip, hip, hooray!"))
						.repeatWhile(r -> ++hoorayCount < 3);
		
		useCaseRunner.run();
	}
	
	public static void main(String[] args){
		new HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample().start();
	}
}