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

# Hello World Example 06 - User enters name and age as normal user, or only age as anonymous user, system prints the data (with validation)
``` java
// Setup useCaseRunner and useCaseModel same as before 

Actor normalUser = useCaseModel.newActor("Normal User");
Actor anonymousUser = useCaseModel.newActor("Anonymous User");
		
useCaseModel.newUseCase("Get greeted (with name and age as normal user, or only with age as anonymous user")
	.basicFlow()
		.newStep(SYSTEM_PROMPTS_USER_TO_ENTER_FIRST_NAME)
			.actors(normalUser).system(promptUserToEnterFirstName())
		.newStep(USER_ENTERS_FIRST_NAME)
			.actors(normalUser).handle(EnterTextEvent.class).system(saveFirstName())
		.newStep(SYSTEM_PROMPTS_USER_TO_ENTER_AGE)
			.actors(normalUser, anonymousUser).system(promptUserToEnterAge())
		.newStep(USER_ENTERS_AGE)
			.actors(normalUser, anonymousUser).handle(EnterTextEvent.class).system(saveAge())
		.newStep(SYSTEM_GREETS_USER_WITH_FIRST_NAME)
			.actors(normalUser).system(greetUserWithFirstName())
		.newStep(SYSTEM_GREETS_USER_WITH_AGE)
			.actors(normalUser, anonymousUser).system(greetUserWithAge())
		.newStep(SYSTEM_TERMINATES_APPLICATION)
			.actors(normalUser, anonymousUser).system(terminateApplication())
	.newFlow("AF1. Handle invalid age").after(USER_ENTERS_AGE).when(ageIsInvalid())
		.newStep(SYSTEM_INFORMS_USER_ABOUT_INVALID_AGE)
			.system(informUserAboutInvalidAge())
		.continueAfter(USER_ENTERS_FIRST_NAME)
	.newFlow("AF2. Handle non-numerical age").after(USER_ENTERS_AGE)
		.newStep(SYSTEM_INFORMS_USER_ABOUT_NON_NUMERICAL_AGE)
			.actors(normalUser, anonymousUser).handle(NumberFormatException.class).system(informUserAboutNonNumericalAge())
		.continueAfter(USER_ENTERS_FIRST_NAME)
	.newFlow("AF3.1 Anonymous User does not enter name").atStart()
		.newStep("Skip step to enter first name")
			.actors(anonymousUser).continueAfter(USER_ENTERS_FIRST_NAME)
	.newFlow("AF3.2 Anonymous User is greeted with age only, not name")
		.after(USER_ENTERS_AGE).when(ageIsInvalid().negate())
			.newStep("Skip step to greet user with first name")
				.actors(anonymousUser).continueAfter(SYSTEM_GREETS_USER_WITH_FIRST_NAME);

useCaseRunner.runAs(anonymousUser);

while(true)
	useCaseRunner.reactTo(enterTextEvent());

// Implementations of the methods ...
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld06_EnterNameAndAgeWithAnonymousUserExample.java).
