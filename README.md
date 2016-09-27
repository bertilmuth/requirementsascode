# requirementsascode
Living specification of application requirements in code, for long term changeability.

Note that as this is an experimental project, the API is likely to change.

# Functional requirements as code
With requirementsascode, you can specify your application's functional requirements in the code itself,
in a form similar to use case specifications.

Benefits of this approach include:
* Perform impact analysis of changes: how does a change to a requirement affect the software components?
* Always up-to-date traceability from requirements to code (inside the code)
* Clear separation between requirements and realization, making technological changes easier

In the future it will also be possible to generate always up-to-date documentation from the code, 
that documents how the application really works.

## Hello World Example
``` java
UseCaseModelRun useCaseModelRun = new UseCaseModelRun();
UseCaseModel useCaseModel = useCaseModelRun.getModel();
		
Actor user = useCaseModel.newActor("User");
		
useCaseModel.newUseCase("Get greeted")
  .basicFlow()
    .newStep("User gets greeted by the system.")
      .system(() -> System.out.println("Hello, user."));
		
useCaseModelRun.as(user);
```
