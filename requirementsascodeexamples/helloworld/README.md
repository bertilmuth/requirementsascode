# Hello World Example 01 - System prints 'Hello, User.'
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
UseCaseModel useCaseModel = useCaseRunner.getUseCaseModel();
		
useCaseModel.newUseCase("Get greeted")
	.basicFlow()
		.newStep("System greets user.")
			.system(() -> System.out.println("Hello, User."));

useCaseRunner.run();
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld01_PrintHelloUserExample.java).

# Hello World Example 02 - System prints 'Hello, User.' and 'Hip, hip, hooray!' three times
``` java
// Setup useCaseRunner and useCaseModel same as before 

useCaseModel.newUseCase("Get greeted")
	.basicFlow()
		.newStep("System greets user.")
			.system(() -> System.out.println("Hello, User."))
		.newStep("System prints 'Hip, hip, hooray!' three times.")
			.system(() -> System.out.println("Hip, hip, hooray!"))
				.repeatWhile(r -> ++hoorayCount < 3);

useCaseRunner.run();

// Implementations of the methods ...
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample.java).


# Hello World Example 03 - User enters name, system prints it
``` java
// Setup useCaseRunner and useCaseModel same as before 

useCaseModel.newUseCase("Get greeted")
	.basicFlow()
		.newStep("System prompts user to enter first name.")
			.system(promptUserToEnterFirstName())
		.newStep("User enters first name. System greets user with first name.")
			.handle(EnterTextEvent.class).system(greetUserWithFirstName())
		.newStep("System terminates application.")
			.system(terminateApplication());

useCaseRunner.run();
useCaseRunner.reactTo(enterTextEvent());

// Implementations of the methods ...
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld03_EnterNameExample.java).

# Hello World Example 04 - User enters name and age, system prints them (exceptions are ignored)
``` java
// Setup useCaseRunner and useCaseModel same as before 

useCaseModel.newUseCase("Get greeted")
	.basicFlow()
		.newStep("System prompts user to enter first name.")
			.system(promptUserToEnterFirstName())
		.newStep("User enters first name. System saves the first name.")
			.handle(EnterTextEvent.class).system(saveFirstName())
		.newStep("System prompts user to enter age.")
			.system(promptUserToEnterAge())
		.newStep("User enters age. System saves age.")
			.handle(EnterTextEvent.class).system(saveAge())
		.newStep("System greets user with first name and age.")
			.system(greetUserWithFirstNameAndAge())
		.newStep("System terminates application.")
			.system(terminateApplication());

useCaseRunner.run();

useCaseRunner.reactTo(enterTextEvent());
useCaseRunner.reactTo(enterTextEvent());

// Implementations of the methods ...
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld04_EnterNameAndAgeExample.java).

# Hello World Example 05 - User enters name and age, system prints them (with validation)
``` java
// Setup useCaseRunner and useCaseModel same as before 

useCaseModel.newUseCase("Get greeted")
	.basicFlow()
		.newStep(SYSTEM_PROMPTS_USER_TO_ENTER_FIRST_NAME)
			.system(promptUserToEnterFirstName())
		.newStep(USER_ENTERS_FIRST_NAME)
			.handle(EnterTextEvent.class).system(saveFirstName())
		.newStep(SYSTEM_PROMPTS_USER_TO_ENTER_AGE)
			.system(promptUserToEnterAge())
		.newStep(USER_ENTERS_AGE)
			.handle(EnterTextEvent.class).system(saveAge())
		.newStep(SYSTEM_GREETS_USER)
			.system(greetUserWithFirstNameAndAge())
		.newStep(SYSTEM_TERMINATES_APPLICATION)
			.system(terminateApplication())
	.newFlow("AF1. Handle invalid age").after(USER_ENTERS_AGE).when(ageIsInvalid())
		.newStep(SYSTEM_INFORMS_USER_ABOUT_INVALID_AGE)
			.system(informUserAboutInvalidAge())
		.continueAfter(USER_ENTERS_FIRST_NAME)
	.newFlow("AF2. Handle non-numerical age").after(USER_ENTERS_AGE)
		.newStep(SYSTEM_INFORMS_USER_ABOUT_NON_NUMERICAL_AGE)
			.handle(NumberFormatException.class).system(informUserAboutNonNumericalAge())
		.continueAfter(USER_ENTERS_FIRST_NAME);

useCaseRunner.run();

while(true)
	useCaseRunner.reactTo(enterTextEvent());	

// Implementations of the methods ...
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld05_EnterNameAndAgeWithValidationExample.java).
