# Hello World Example 01 - System prints 'Hello, User.'
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
		
useCaseRunner.useCaseModel().useCase("Get greeted")
	.basicFlow()
		.step("S1").system(greetUser());

useCaseRunner.run();
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld01_PrintHelloUserExample.java).

# Hello World Example 02 - System prints 'Hello, User.' and 'Hip, hip, hooray!' three times
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
		
useCaseRunner.useCaseModel().useCase("Get greeted")
	.basicFlow()
		.step("S1").system(greetUser())
		.step("S2").system(printHooray())
			.repeatWhile(thereAreLessThanThreeHoorays());

useCaseRunner.run();
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample.java).


# Hello World Example 03 - User enters first name, system prints it
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
		
useCaseRunner.useCaseModel().useCase("Get greeted")
	.basicFlow()
		.step("S1").system(promptUserToEnterFirstName())
		.step("S2").user(ENTER_FIRST_NAME).system(greetUserWithFirstName())
		.step("S3").system(terminateApplication());

useCaseRunner.run();
useCaseRunner.reactTo(enterText());
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld03_EnterNameExample.java).

# Hello World Example 04 - User enters name and age, system prints them (exceptions are ignored)
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
		
useCaseRunner.useCaseModel().useCase("Get greeted")
	.basicFlow()
		.step("S1").system(promptUserToEnterFirstName())
		.step("S2").user(ENTER_FIRST_NAME).system(saveFirstName())
		.step("S3").system(promptUserToEnterAge())
		.step("S4").user(ENTER_AGE).system(saveAge())
		.step("S5").system(greetUserWithFirstNameAndAge())
		.step("S6").system(terminateApplication());

useCaseRunner.run();

useCaseRunner.reactTo(enterText());
useCaseRunner.reactTo(enterText());
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld04_EnterNameAndAgeExample.java).

# Hello World Example 05 - User enters name and age, system prints them (with validation)
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
		
useCaseRunner.useCaseModel().useCase("Get greeted")
	.basicFlow()
		.step("S1").system(promptUserToEnterFirstName())
		.step(S2).user(ENTER_FIRST_NAME).system(saveFirstName())
		.step("S3").system(promptUserToEnterAge())
		.step(S4).user(ENTER_AGE).system(saveAge())
		.step("S5").system(greetUserWithFirstNameAndAge())
		.step("S6").system(terminateApplication())
			
	.flow("Handle out-of-bounds age").after(S4).when(ageIsOutOfBounds())
		.step("S4a_1").system(informUserAboutOutOfBoundsAge())
		.continueAfter(S2)
			
	.flow("Handle non-numerical age").after(S4)
		.step("S4b_1").handle(NON_NUMERICAL_AGE)
			.system(informUserAboutNonNumericalAge())
		.continueAfter(S2);

useCaseRunner.run();

while(true)
	useCaseRunner.reactTo(enterText());	
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld05_EnterNameAndAgeWithValidationExample.java).

# Hello World Example 06 - User enters name and age as normal user, or only age as anonymous user, system prints the data (with validation)
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
UseCaseModel useCaseModel = useCaseRunner.useCaseModel();

Actor normalUser = useCaseModel.actor("Normal User");
Actor anonymousUser = useCaseModel.actor("Anonymous User");
		
useCaseModel.useCase("Get greeted")
	.basicFlow()
		.step("S1").as(normalUser).system(promptUserToEnterFirstName())
		.step(S2).as(normalUser).user(ENTER_FIRST_NAME).system(saveFirstName())
		.step("S3").as(normalUser, anonymousUser).system(promptUserToEnterAge())
		.step(S4).as(normalUser, anonymousUser).user(ENTER_AGE).system(saveAge())
		.step("S5").as(normalUser).system(greetUserWithFirstName())
		.step("S6").as(normalUser, anonymousUser).system(greetUserWithAge())
		.step("S7").as(normalUser, anonymousUser).system(terminateApplication())
			
	.flow("Handle out-of-bounds age").after(S4).when(ageIsOutOfBounds())
		.step("S4a_1").system(informUserAboutOutOfBoundsAge())
		.continueAfter(S2)
			
	.flow("Handle non-numerical age").after(S4)
		.step("S4b_1").handle(NON_NUMERICAL_AGE)
			.system(informUserAboutNonNumericalAge())
		.continueAfter(S2)
		
	.flow("Anonymous greeted with age only").after(S4).when(ageIsOk())
		.step("S4c_1").as(anonymousUser).continueAfter(S5)
		
	.flow("Anonymous does not enter name").atStart()
		.step("S0a").as(anonymousUser).continueAfter(S2);	

useCaseRunner.runAs(anonymousUser);

while(true)
	useCaseRunner.reactTo(enterText());		
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld06_EnterNameAndAgeWithAnonymousUserExample.java).
