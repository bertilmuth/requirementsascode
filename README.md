# requirements as code
As soon as a requirement is implemented, the code becomes the most reliable "source of truth". 

Writing functional requirements as use cases in the code, and being able to run them - that is what this project is about. 

Later, you can automatically generate living documentation from the use cases. This documentation accurately reflects the visible behavior of your application.

In addition to that, you can handle cross-cutting concerns in a very simple way, e.g. for measuring performance.

While requirements as code works with any application design, it makes it specifically easy to transition your application to a clean architecture.

# getting started
At least Java 8 is required, download and install it if necessary.

Requirements as code is available on Maven Central.

If you are using Maven, include the following in your POM, to use the core:
``` xml
  <dependency>
    <groupId>org.requirementsascode</groupId>
    <artifactId>requirementsascodecore</artifactId>
    <version>0.6.0</version>
  </dependency>
```

If you are using Gradle, include the following in your build.gradle, to use the core:
```
compile 'org.requirementsascode:requirementsascodecore:0.6.0'
```

Then, for a simple Hello World example, run the following code (e.g. in a main method):
``` java
UseCaseModel useCaseModel = 
  UseCaseModelBuilder.newBuilder()
    .useCase("Say Hello")
      .basicFlow()
        .step("S1").system(r -> System.out.println("Hello World!"))
  .build();
UseCaseModelRunner runner = new UseCaseModelRunner();
runner.run(useCaseModel);
```

# documentation
* [more hello world examples for building/running use case models](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/helloworld)
* [how to extract use cases from code, and generate documentation](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeextract)
* [cross-cutting concerns example](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/crosscuttingconcerns)

# benefits of using requirements as code
As the same code controls the visible behavior of your software _and_ is used as the source of documentation,
you can always generate documentation that represents how your software really works. Also, there is a defined place in your code that describes the use cases and how they are realized.
That makes your application more maintable.

While requirements as code works with any application design, it makes it easy to transition your application
to a [clean architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html). 
By reducing dependencies between the application's components, you can make your application more changeable.
A clear separation between user interface and backend makes it easier to test the application without user interface, 
and vary or change the user interface.

# subprojects
* [requirementsascodecore](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodecore): create and run use case models. 
* [requirementsascodeextract](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeextract): extract use cases from code and generate documentation from it.
* [requirementsascodeexamples](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples): example projects illustrating the use of requirementsascodecore.

# related topics
* The work of Ivar Jacobson on Use Cases. As an example, have a look at [Use Case 2.0](https://www.ivarjacobson.com/publications/white-papers/use-case-ebook).
* The work of Alistair Cockburn on Use Cases, specifically the different goal levels. Look [here](http://alistair.cockburn.us/Use+case+fundamentals) to get started, or read the book "Writing Effective Use Cases".
* Behavior Driven Development ([BDD](https://dannorth.net/introducing-bdd/)): There are similarities between BDD and requirements as code. Both try to provide a "ubiquitous language" to be spoken by both business people and developers. While the focus of BDD is on specific examples that drive the implementation (and may or may not be executed by automated tests), the focus of requirements as code is on long-term maintenance of requirements in production code. While examples and tests focus on the specific state or behavior resulting from a specific input to the software, the requirements describe the software's behavior in general terms. 
* The Boundary Control Entity Pattern ([BCE](http://epf.eclipse.org/wikis/openup/core.tech.common.extend_supp/guidances/guidelines/entity_control_boundary_pattern_C4047897.html)): The UseCaseModelRunner in requirements as code plays the role of the Control class in BCE, but it is configured by an underlying UseCaseModel.
* Robert C. Martin's [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) and the architectures it is based upon.
