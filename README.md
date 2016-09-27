# requirementsascode
Living specification of application requirements in code, for long term changeability.

Note that as this is an experimental project, the API is likely to change.

Subprojects:
* [requirementsascodecore](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodecore): the core library to be used, with no third-party dependencies.
* [requirementsascodesamples](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodesamples): sample projects illustrating the use of requirementsascodecore.

# Benefits
With requirementsascode, you can specify your application's functional requirements in the code itself,
in a form similar to use case specifications.

Benefits of this approach include:
* Perform impact analysis of changes: how does a change to a requirement affect the software components?
* Always up-to-date traceability from requirements to code (inside the code)
* Clear separation between requirements and realization, making technological changes easier

In the future it will also be possible to generate always up-to-date documentation from the code, 
that documents how the application really works.

# Hello World Example - System prints "Hello, User."
``` java
UseCaseModelRun useCaseModelRun = new UseCaseModelRun();
UseCaseModel useCaseModel = useCaseModelRun.getModel();

Actor user = useCaseModel.newActor("User");

useCaseModel.newUseCase("Get greeted")
	.basicFlow()
		.newStep("System greets user.")
			.system(() -> System.out.println("Hello, User."));

useCaseModelRun.as(user);
```

# Hello World Example - User enters name, system prints it
``` java
// Setup useCaseModelRun, useCaseModel and user same as before 

useCaseModel.newUseCase("Get greeted")
	.basicFlow()
		.newStep("User enters first name. System greets user with first name.")
			.actor(user, EnterFirstName.class)
			.system(greetUser());

useCaseModelRun.as(user);
useCaseModelRun.reactTo(enterFirstNameEvent());

// Implementations of the methods ...
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodesamples/helloworld/src/main/java/helloworld/HelloWorld02_UserEntersName.java).
