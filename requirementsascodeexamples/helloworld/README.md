# hello world 01 - system prints 'Hello, User.'
``` java		
public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
	UseCaseModel useCaseModel = 
		modelBuilder.useCase("Get greeted")
			.basicFlow()
				.step("S1").system(this::greetsUser)
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
				.step("S1").system(this::greetsUser)
				.step("S2").system(this::printsHooray)
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
				.step("S1").system(this::promptsUserToEnterFirstName)
				.step("S2").user(ENTER_FIRST_NAME).system(this::greetsUserWithFirstName)
		.build();
	return useCaseModel;
}
...
UseCaseModel useCaseModel = buildWith(UseCaseModelBuilder.newBuilder());
UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
useCaseModelRunner.run(useCaseModel);
useCaseModelRunner.reactTo(entersText());
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld03.java).

# hello world 04 - user enters name and age, system prints them (UnhandledException thrown if non-numerical age entered)
``` java
public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
	UseCaseModel useCaseModel = 
		modelBuilder.useCase("Get greeted")
			.basicFlow()
				.step("S1").system(this::promptsUserToEnterFirstName)
				.step("S2").user(ENTERS_FIRST_NAME).system(this::savesFirstName)
				.step("S3").system(this::promptsUserToEnterAge)
				.step("S4").user(ENTERS_AGE).system(this::savesAge)
				.step("S5").system(this::greetsUserWithFirstNameAndAge)
		.build();
	return useCaseModel;
}
...
UseCaseModel useCaseModel = buildWith(UseCaseModelBuilder.newBuilder());
UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
useCaseModelRunner.run(useCaseModel);
useCaseModelRunner.reactTo(entersText());
useCaseModelRunner.reactTo(entersText());	
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld04.java).

# hello world 05 - user enters name and age, system prints them (with validation)
``` java
public UseCaseModel buildWith(UseCaseModelBuilder modelBuilder) {
	UseCaseModel useCaseModel = 
		modelBuilder.useCase("Get greeted")
			.basicFlow()
				.step("S1").system(this::promptsUserToEnterFirstName)
				.step("S2").user(ENTERS_FIRST_NAME).system(this::savesFirstName)
				.step("S3").system(this::promptsUserToEnterAge)
				.step("S4").user(ENTERS_AGE).system(this::savesAge)
				.step("S5").system(this::greetsUserWithFirstNameAndAge)
				.step("S6").system(this::stops)
					
			.flow("Handle out-of-bounds age").insteadOf("S5").when(this::ageIsOutOfBounds)
				.step("S5a_1").system(this::informsUserAboutOutOfBoundsAge)
				.step("S5a_2").continuesAt("S3")
					
			.flow("Handle non-numerical age").insteadOf("S5")
				.step("S5b_1").handles(NON_NUMERICAL_AGE).system(this::informsUserAboutNonNumericalAge)
				.step("S5b_2").continuesAt("S3")
		.build();
	return useCaseModel;
}	
...
UseCaseModel useCaseModel = buildWith(UseCaseModelBuilder.newBuilder());
UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
useCaseModelRunner.run(useCaseModel);			
while(!systemStopped())
	useCaseModelRunner.reactTo(entersText());	
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
				.step("S1").as(normalUser).system(this::promptsUserToEnterFirstName)
				.step("S2").as(normalUser).user(ENTERS_FIRST_NAME).system(this::savesFirstName)
				.step("S3").as(normalUser, anonymousUser).system(this::promptsUserToEnterAge)
				.step("S4").as(normalUser, anonymousUser).user(ENTERS_AGE).system(this::savesAge)
				.step("S5").as(normalUser).system(this::greetsUserWithFirstName)
				.step("S6").as(normalUser, anonymousUser).system(this::greetsUserWithAge)
				.step("S7").as(normalUser, anonymousUser).system(this::stops)
					
			.flow("Handle out-of-bounds age").insteadOf("S5").when(this::ageIsOutOfBounds)
				.step("S5a_1").system(this::informsUserAboutOutOfBoundsAge)
				.step("S5a_2").continuesAt("S3")
					
			.flow("Handle non-numerical age").insteadOf("S5")
				.step("S5b_1").handles(NON_NUMERICAL_AGE).system(this::informsUserAboutNonNumericalAge)
				.step("S5b_2").continuesAt("S3")
				
			.flow("Anonymous greeted with age only").insteadOf("S5").when(this::ageIsOk)
				.step("S5c_1").as(anonymousUser).continuesAt("S6")
				
			.flow("Anonymous does not enter name").insteadOf("S1")
				.step("S1a_1").as(anonymousUser).continuesAt("S3")
		.build();
	return useCaseModel;
}
...
UseCaseModel useCaseModel = buildWith(UseCaseModelBuilder.newBuilder());
UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
useCaseModelRunner.as(anonymousUser()).run(useCaseModel);			
while(!systemStopped())
	useCaseModelRunner.reactTo(entersText());	
exitSystem();	
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld06.java).
