# requirementsascode
Living specification of application requirements in code, for long term changeability.

Note that as this is an experimental project, the API is likely to change.
At least Java 8 is required.

Subprojects:
* [requirementsascodecore](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodecore): the core library to be used, with no third-party dependencies.
* [requirementsascodeexamples](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples): example projects illustrating the use of requirementsascodecore.

A good place to start are the [helloworld samples](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/helloworld)

# Why bother?
With requirementsascode, you can specify your application's functional requirements in the code itself,
in a form similar to use case specifications.

Benefits of this approach include:
* Perform impact analysis of changes: how does a change to a requirement affect the software components?
* Always up-to-date traceability from requirements to code (inside the code)
* Clear separation between requirements and realization, making technological changes easier

In the future it will also be possible to generate always up-to-date documentation from the code 
that documents how the application really works.
