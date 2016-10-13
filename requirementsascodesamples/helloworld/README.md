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
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodesamples/helloworld/src/main/java/helloworld/HelloWorld01_SystemPrintsHelloUserExample.java).

# Hello World Sample - User enters name, system prints it
``` java
// Setup useCaseRunner and useCaseModel same as before 

useCaseModel.newUseCase("Get greeted")
	.basicFlow()
		.newStep("User enters first name. System greets user with first name.")
			.handle(EnterTextEvent.class).system(greetUserWithFirstName());

useCaseRunner.run();

String firstName = enterText("Please enter your first name: ");
useCaseRunner.reactTo(new EnterTextEvent(firstName));

// Implementations of the methods ...
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodesamples/helloworld/src/main/java/helloworld/HelloWorld02_UserEntersNameExample.java).

# Hello World Sample - User enters name and age, system prints it (no exception handling, entering non-numerical age is ignore)
``` java
// Setup useCaseRunner and useCaseModel same as before 

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

// Implementations of the methods ...
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodesamples/helloworld/src/main/java/helloworld/HelloWorld03_UserEntersNameAndAgeExample.java).
