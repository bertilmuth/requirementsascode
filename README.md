# requirementsascode
As long as a requirement is still under discussion, it makes sense to have a lightweight documentation of it outside the code, maybe a user story.
As soon as a requirement is implemented, the code itself becomes the most reliable "source of truth" for the requirement and its realization.

With requirementsascode,  you can represent user journeys as executable specifications in your application source code.  The form of the representation is similar to [user goal level use case](https://en.wikipedia.org/wiki/Use_case#Goal_levels) narratives.
Later, you can generate documentation from the same code (not some documentation you insert into the code!). 
The documentation describes the user journeys in a non-technical way,
so that a larger group of stakeholders can understand it.

# benefits
As the same code controls the visible behavior of your software and is used as the source of documentation,
you can always generate documentation that is highly likely to represent how your software really works. Also, there is a defined place in your code that describes the requirements and how they are realized.
That makes your application more maintable.

While requirementsascode works with any application design, it makes it easy to transition your application
to a [clean architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html). 
By reducing dependencies between the application's components, you can make your application more changeable.
A clear separation between user interface and backend makes it easier to test the application without user interface, 
and vary or change the user interface.

In addition to that, you can also specify cross-cutting concerns, e.g. for measuring performance.

# documentation
* [hello world examples](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/helloworld)
* [cross-cutting concerns examples](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/crosscuttingconcerns)
* [javadoc](https://github.com/bertilmuth/requirementsascode/releases/download/v0.4.0/requirementsascodecore-0.4.0-javadoc.jar)

Note that the API is likely to change.

# getting started
* At least Java 8 is required, download and install it if necessary.
* To use the framework, download the [requirementsascodecore jar](https://github.com/bertilmuth/requirementsascode/releases/download/v0.4.0/requirementsascodecore-0.4.0.jar) and save it in a local folder.
* If you want to try out the Hello World examples, download the [helloworld jar](https://github.com/bertilmuth/requirementsascode/releases/download/v0.4.0/helloworld-0.4.0.jar) and save it in the same folder.
* To run the first Hello World example, switch to the folder in a console and enter: 

Windows:
``` java
java -cp "helloworld-0.4.0.jar;requirementsascodecore-0.4.0.jar" helloworld.HelloWorld01_PrintHelloUserExample
```
Unix:
``` java
java -cp "helloworld-0.4.0.jar:requirementsascodecore-0.4.0.jar" helloworld.HelloWorld01_PrintHelloUserExample
```
The same principle applies to the cross cutting concerns example projects: you need to put the example project's jar
on the classpath, and the requirementsascodecore jar.

For the shoppingappjavafx and shoppingappextract examples, a distribution zip is available in the Releases tab
that makes it easy to start the application.

Note that if you want to use the shoppingappjavafx example in Eclipse, you should use
the e(fx)clipse plugin.

Build tool support will be available in the future.

# subprojects
* [requirementsascodecore](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodecore): the core library to be used, with no third-party dependencies.
* [requirementsascodeexamples](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples): example projects illustrating the use of requirementsascodecore.

# related topics
* The work of Ivar Jacobson on Use Cases. As an example, have a look at [Use Case 2.0](https://www.ivarjacobson.com/publications/white-papers/use-case-ebook).
* The work of Alistair Cockburn on Use Cases, specifically the different goal levels. Look [here](http://alistair.cockburn.us/Use+case+fundamentals) to get started, or read the book "Writing Effective Use Cases".
* Behavior Driven Development ([BDD](https://dannorth.net/introducing-bdd/)): There are similarities between BDD and requirementsascode. Both try to provide a "ubiquitous language" to be spoken by both business people and developers. While the focus of BDD is on specific examples that drive the implementation (and may or may not be executed by automated tests), the focus of requirementsascode is on long-term maintenance of requirements in production code. While examples and tests focus on the specific state or behavior resulting from a specific input to the software, the requirements describe the software's behavior in general terms. 
* The Boundary Control Entity Pattern ([BCE](http://epf.eclipse.org/wikis/openup/core.tech.common.extend_supp/guidances/guidelines/entity_control_boundary_pattern_C4047897.html)): The UseCaseModelRunner in requirementsascode plays the role of the Control class in BCE, but it is configured by an underlying UseCaseModel.
* Robert C. Martin's [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) and the architectures it is based upon.
