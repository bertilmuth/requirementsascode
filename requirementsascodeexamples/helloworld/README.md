# hello world 01 - system prints 'Hello, User.'
``` java		
public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
	UseCaseModel useCaseModel = 
		modelBuilder.useCase("Get greeted")
			.basicFlow()
				.step("S1").system(this::greetUser)
		.build();
	return useCaseModel;
}
...
UseCaseModel useCaseModel = buildWith(UseCaseModelBuilder.newBuilder());
new UseCaseModelRunner().run(useCaseModel);
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld01.java).

# hello world 02 - system prints 'Hello, User.' and 'Hip, hip, hooray!' three times
``` java		
public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
	UseCaseModel useCaseModel = 
		modelBuilder.useCase("Get greeted")
			.basicFlow()
				.step("S1").system(this::greetUser)
				.step("S2").system(this::printHooray)
					.reactWhile(this::lessThanThreeHooraysHaveBeenPrinted)
		.build();
	
	return useCaseModel;
}
...
UseCaseModel useCaseModel = buildWith(UseCaseModelBuilder.newBuilder());
new UseCaseModelRunner().run(useCaseModel);
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld02.java).


# hello world 03 - user enters first name, system prints it
``` java
public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
	UseCaseModel useCaseModel = 
		modelBuilder.useCase("Get greeted")
			.basicFlow()
				.step("S1").system(this::promptUserToEnterFirstName)
				.step("S2").user(ENTER_FIRST_NAME).system(this::greetUserWithFirstName)
		.build();
	return useCaseModel;
}
...
UseCaseModel useCaseModel = buildWith(UseCaseModelBuilder.newBuilder());
UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
useCaseModelRunner.run(useCaseModel);
useCaseModelRunner.reactTo(enterText());
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld03.java).

# hello world 04 - user enters name and age, system prints them (UnhandledException thrown if non-numerical age entered)
``` java
public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
	UseCaseModel useCaseModel = 
	        modelBuilder.useCase("Get greeted")
		        .basicFlow()
				.step("S1").system(this::promptUserToEnterFirstName)
				.step("S2").user(ENTER_FIRST_NAME).system(this::saveFirstName)
				.step("S3").system(this::promptUserToEnterAge)
				.step("S4").user(ENTER_AGE).system(this::saveAge)
				.step("S5").system(this::greetUserWithFirstNameAndAge)
		.build();
	return useCaseModel;
}
...
UseCaseModel useCaseModel = buildWith(UseCaseModelBuilder.newBuilder());
UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
useCaseModelRunner.run(useCaseModel);
useCaseModelRunner.reactTo(enterText());
useCaseModelRunner.reactTo(enterText());	
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld04.java).

# hello world 05 - user enters name and age, system prints them (with validation)
``` java
public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
	UseCaseModel useCaseModel = 
		modelBuilder.useCase("Get greeted")
			.basicFlow()
				.step("S1").system(this::promptUserToEnterFirstName)
				.step("S2").user(ENTER_FIRST_NAME).system(this::saveFirstName)
				.step("S3").system(this::promptUserToEnterAge)
				.step("S4").user(ENTER_AGE).system(this::saveAge)
				.step("S5").system(this::greetUserWithFirstNameAndAge)
				.step("S6").system(this::stopSystem)
						
			.flow("Handle out-of-bounds age").insteadOf("S5").when(this::ageIsOutOfBounds)
				.step("S5a_1").system(this::informUserAboutOutOfBoundsAge)
				.step("S5a_2").continueAt("S3")
						
			.flow("Handle non-numerical age").insteadOf("S5")
				.step("S5b_1").handle(NON_NUMERICAL_AGE).system(this::informUserAboutNonNumericalAge)
				.step("S5b_2").continueAt("S3")
		.build();
	return useCaseModel;
}	
...
UseCaseModel useCaseModel = buildWith(UseCaseModelBuilder.newBuilder());
UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
useCaseModelRunner.run(useCaseModel);			
while(!systemStopped())
	useCaseModelRunner.reactTo(enterText());	
exitSystem();	
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld05.java).

# hello world 06 - user enters name and age as normal user, or only age as anonymous user, system prints the data (with validation)
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
UseCaseModel useCaseModel = useCaseRunner.useCaseModel();
...
public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
	normalUser = modelBuilder.actor("Normal User");
	anonymousUser = modelBuilder.actor("Anonymous User");
			
	UseCaseModel useCaseModel = 
		modelBuilder.useCase("Get greeted")
			.basicFlow()
				.step("S1").as(normalUser).system(this::promptUserToEnterFirstName)
				.step("S2").as(normalUser).user(ENTER_FIRST_NAME).system(this::saveFirstName)
				.step("S3").as(normalUser, anonymousUser).system(this::promptUserToEnterAge)
				.step("S4").as(normalUser, anonymousUser).user(ENTER_AGE).system(this::saveAge)
				.step("S5").as(normalUser).system(this::greetUserWithFirstName)
				.step("S6").as(normalUser, anonymousUser).system(this::greetUserWithAge)
				.step("S7").as(normalUser, anonymousUser).system(this::stopSystem)
					
			.flow("Handle out-of-bounds age").insteadOf("S5").when(this::ageIsOutOfBounds)
				.step("S5a_1").system(this::informUserAboutOutOfBoundsAge)
				.step("S5a_2").continueAt("S3")
					
			.flow("Handle non-numerical age").insteadOf("S5")
				.step("S5b_1").handle(NON_NUMERICAL_AGE).system(this::informUserAboutNonNumericalAge)
				.step("S5b_2").continueAt("S3")
				
			.flow("Anonymous greeted with age only").insteadOf("S5").when(this::ageIsOk)
				.step("S5c_1").as(anonymousUser).continueAt("S6")
				
			.flow("Anonymous does not enter name").insteadOf("S1")
				.step("S1a_1").as(anonymousUser).continueAt("S3")
		.build();
	return useCaseModel;
}
...
UseCaseModel useCaseModel = buildWith(UseCaseModelBuilder.newBuilder());
UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
useCaseModelRunner.as(anonymousUser()).run(useCaseModel);			
while(!systemStopped())
	useCaseModelRunner.reactTo(enterText());	
exitSystem();	
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld06.java).
