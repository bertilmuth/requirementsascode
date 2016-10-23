# requirementsascode
Living specification of application requirements in code, for long term changeability.

Note that as this is an experimental project, the API is likely to change.
At least Java 8 is required.

Subprojects:
* [requirementsascodecore](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodecore): the core library to be used, with no third-party dependencies.
* [requirementsascodeexamples](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples): example projects illustrating the use of requirementsascodecore.

A good place to start are the [helloworld examples](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/helloworld).

# Why bother?
This project is about documenting requirements in programming language code, as part of a software application.

Documentation of requirements provides value, not only when a feature is created, but especially when requirements change later on. However, requirements documentation tends to get outdated very quickly, once code has been developed - "the truth is in the code" is an often quoted statement.

That is the reason why many agile teams do not maintain requirements after they have been implemented, for example they throw away the user stories. The risk of that approach is that over time, it becomes unclear *where* in the code *what changes* need to be performed when a requirement changes. This is especially true if many teams and developers work on an application, with different approaches to fulfilling the requirements inside the code. 

As long as ideas are exchanged between stakeholders, and the requirements are discussed, but not implemented, it makes sense to have a lightweight documentation of requirements outside the code. Maybe as part of a backlog or a lightweight specification.

But for the long term changeability of a requirement, once it has been implemented, the project proposes a way to document the requirement in the code itself. That means: to have a specific part of the code that BOTH controls the behavior of the application, and documents the requirements in a formal way. 

Benefits of this approach include:
* Perform impact analysis of changes: how does a change to a requirement affect the software components?
* Always up-to-date traceability from requirements to code (inside the code)
* Clear separation between requirements and realization, making technological changes easier

In the future it will also be possible to generate always up-to-date documentation from the code 
that documents how the application really works.
