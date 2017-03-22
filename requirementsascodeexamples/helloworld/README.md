# hello world example 01 - system prints 'Hello, User.'
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
UseCaseModel useCaseModel = useCaseRunner.useCaseModel();
...		
useCaseModel().useCase("Get greeted")
	.basicFlow()
		.step("S1").system(greetUser());

useCaseRunner.run();
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld01_PrintHelloUserExample.java).

# hello world example 02 - system prints 'Hello, User.' and 'Hip, hip, hooray!' three times
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
UseCaseModel useCaseModel = useCaseRunner.useCaseModel();
...		
useCaseModel.useCase("Get greeted")
	.basicFlow()
		.step("S1").system(greetUser())
		.step("S2").system(printHooray())
			.reactWhile(lessThanThreeHooraysHaveBeenPrinted());
...
useCaseRunner.run();
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld02_PrintHelloUserAndHipHipHoorayThreeTimesExample.java).


# hello world example 03 - user enters first name, system prints it
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
UseCaseModel useCaseModel = useCaseRunner.useCaseModel();
...		
useCaseModel().useCase("Get greeted")
	.basicFlow()
		.step("S1").system(promptUserToEnterFirstName())
		.step("S2").user(ENTER_FIRST_NAME).system(greetUserWithFirstName());
...
useCaseRunner.run();
useCaseRunner.reactTo(example.enterText());
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld03_EnterNameExample.java).

# hello world example 04 - user enters name and age, system prints them (UnhandledException thrown if non-numerical age entered)
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
UseCaseModel useCaseModel = useCaseRunner.useCaseModel();
...		
useCaseModel().useCase("Get greeted")
	.basicFlow()
		.step("S1").system(promptUserToEnterFirstName())
		.step("S2").user(ENTER_FIRST_NAME).system(saveFirstName())
		.step("S3").system(promptUserToEnterAge())
		.step("S4").user(ENTER_AGE).system(saveAge())
		.step("S5").system(greetUserWithFirstNameAndAge());
...
useCaseRunner.run();
useCaseRunner.reactTo(example.enterText());
useCaseRunner.reactTo(example.enterText());
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld04_EnterNameAndAgeExample.java).

# hello world example 05 - user enters name and age, system prints them (with validation)
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
UseCaseModel useCaseModel = useCaseRunner.useCaseModel();
...		
useCaseModel.useCase("Get greeted")
	.basicFlow()
		.step("S1").system(promptUserToEnterFirstName())
		.step(S2).user(ENTER_FIRST_NAME).system(saveFirstName())
		.step("S3").system(promptUserToEnterAge())
		.step(S4).user(ENTER_AGE).system(saveAge())
		.step(S5).system(greetUserWithFirstNameAndAge())
		.step("S6").system(stopSystem())
			
	.flow("Handle out-of-bounds age").insteadOf(S5).when(ageIsOutOfBounds())
		.step("S5a_1").system(informUserAboutOutOfBoundsAge())
		.step("S5a_2").continueAfter(S2)
			
	.flow("Handle non-numerical age").insteadOf(S5)
		.step("S5b_1").handle(NON_NUMERICAL_AGE).system(informUserAboutNonNumericalAge())
		.step("S5b_2").continueAfter(S2);		
...
useCaseRunner.run();
while(!example.systemStopped())
	useCaseRunner.reactTo(example.enterText());	
example.exitSystem();
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld05_EnterNameAndAgeWithValidationExample.java).

# hello world example 06 - user enters name and age as normal user, or only age as anonymous user, system prints the data (with validation)
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
UseCaseModel useCaseModel = useCaseRunner.useCaseModel();
...
normalUser = useCaseModel.actor("Normal User");
anonymousUser = useCaseModel.actor("Anonymous User");
		
useCaseModel.useCase("Get greeted")
	.basicFlow()
		.step(S1).as(normalUser).system(promptUserToEnterFirstName())
		.step(S2).as(normalUser).user(ENTER_FIRST_NAME).system(saveFirstName())
		.step(S3).as(normalUser, anonymousUser).system(promptUserToEnterAge())
		.step(S4).as(normalUser, anonymousUser).user(ENTER_AGE).system(saveAge())
		.step(S5).as(normalUser).system(greetUserWithFirstName())
		.step(S6).as(normalUser, anonymousUser).system(greetUserWithAge())
		.step("S7").as(normalUser, anonymousUser).system(stopSystem())
			
	.flow("Handle out-of-bounds age").insteadOf(S5).when(ageIsOutOfBounds())
		.step("S5a_1").system(informUserAboutOutOfBoundsAge())
		.step("S5a_2").continueAt(S3)
			
	.flow("Handle non-numerical age").insteadOf(S5)
		.step("S5b_1").handle(NON_NUMERICAL_AGE).system(informUserAboutNonNumericalAge())
		.step("S5b_2").continueAt(S3)
		
	.flow("Anonymous greeted with age only").insteadOf(S5).when(ageIsOk())
		.step("S5c_1").as(anonymousUser).continueAt(S6)
		
	.flow("Anonymous does not enter name").insteadOf(S1)
		.step("S1a_1").as(anonymousUser).continueAt(S3);	
...
useCaseRunner.runAs(example.anonymousUser());			
while(!example.systemStopped())
	useCaseRunner.reactTo(example.enterText());	
example.exitSystem();
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld06_EnterNameAndAgeWithAnonymousUserExample.java).
