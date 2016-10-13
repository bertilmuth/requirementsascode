# Hello World Sample - System prints "Hello, User."
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
UseCaseModel useCaseModel = useCaseRunner.getUseCaseModel();
		
useCaseModel.newUseCase("Get greeted")
	.basicFlow()
		.newStep("System greets user.")
			.system(() -> System.out.println("Hello, User."));

useCaseRunner.run();
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodesamples/helloworld/src/main/java/helloworld/HelloWorld01_SystemPrintsHelloUser.java).

# Hello World Sample - User enters name, system prints it
``` java
// Setup useCaseRunner and useCaseModel same as before 

useCaseModel.newUseCase("Get greeted")
	.basicFlow()
		.newStep("User enters first name. System greets user with first name.")
			.handle(EnterFirstNameEvent.class)
			.system(greetUser());

useCaseRunner.run();

String firstName = enterFirstNameInConsole();
useCaseRunner.reactTo(new EnterFirstNameEvent(firstName));

// Implementations of the methods ...
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodesamples/helloworld/src/main/java/helloworld/HelloWorld02_UserEntersName.java).
