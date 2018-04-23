# requirements as code
Starting to develop an event driven application can be challenging.
This project aims to simplify it. 

The project provides a concise way to create handlers for many types of events at once.

A single runner receives events, and dispatches them to the handlers. That can be used for replay in event sourced applications.

For more advanced scenarios that depend on the application's state, 
you create a use case model with flows.
It's an easy alternative to state machines,
understandable by developers and business people alike.

For the long term maintenance of your application,
you [generate documentation](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeextract) 
from the models inside the code without the need to add comments to it.

You can also handle [cross-cutting concerns](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/crosscuttingconcerns) in a simple way, for example for measuring performance.

# getting started
At least Java 8 is required, download and install it if necessary.

Requirements as code is available on Maven Central.

If you are using Maven, include the following in your POM, to use the core:

``` xml
  <dependency>
    <groupId>org.requirementsascode</groupId>
    <artifactId>requirementsascodecore</artifactId>
    <version>0.7.0</version>
  </dependency>
```

If you are using Gradle, include the following in your build.gradle, to use the core:

```
compile 'org.requirementsascode:requirementsascodecore:0.7.0'
```
# how to use requirements as code
Here's what you need to do as a developer:

## Step 1: Build a model defining the event classes to handle, and the methods that react to events:
``` java
Model model = Model.builder()
	.handles(<Event Class Name>.class).with(<Reference To Method That Handles Event>)
	.handles(..).with(...)
	...
.build()
```

The order of the statements has no significance.
For handling exceptions instead of events, use the specific exception's class or Throwable.class.
Use when before handles to define an additional condition that must be fulfilled.

## Step 2: Create a runner and run the model:
``` java
ModelRunner runner = new ModelRunner();
runner.run(model);
```

## Step 3: Send events to the runner, and enjoy watching it react:
``` java
runner.reactTo(<Event POJO Object>);
```
If an event's class is not declared in the model, the runner consumes it silently.
If an exception is thrown in one of the handler methods and it is not handled by any 
other handler method, the runner will throw an  unchecked UnhandledException
that wraps it.

# hello world
Here's a complete Hello World example:

``` java
package hello;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

public class HelloUser {
	public static void main(String[] args) {
		Model model = Model.builder()
			.handles(NameEntered.class).with(HelloUser::displayEnteredName)
		.build();

		ModelRunner runner = new ModelRunner();
		runner.run(model);
		runner.reactTo(new NameEntered("Joe"));
	}

	public static void displayEnteredName(NameEntered nameEntered) {
		System.out.println("Hello, " + nameEntered.getUserName());
	}

	static class NameEntered {
		private String userName;

		public NameEntered(String userName) {
			this.userName = userName;
		}

		public String getUserName() {
			return userName;
		}
	}
}
```

# documentation
* [examples for building/running more sophisticated use case models](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/helloworld)
* [how to extract use cases from code, and generate documentation](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeextract)
* [cross-cutting concerns example](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/crosscuttingconcerns)

# subprojects
* [requirements as code core](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodecore): create and run use case models. 
* [requirements as code extract](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeextract): extract use cases from code and generate documentation from it.
* [requirements as code examples](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples): example projects illustrating the use of requirements as code core.

# related topics
* The work of Ivar Jacobson on Use Cases. As an example, have a look at [Use Case 2.0](https://www.ivarjacobson.com/publications/white-papers/use-case-ebook).
* The work of Alistair Cockburn on Use Cases, specifically the different goal levels. Look [here](http://alistair.cockburn.us/Use+case+fundamentals) to get started, or read the book "Writing Effective Use Cases".
* Behavior Driven Development ([BDD](https://dannorth.net/introducing-bdd/)): There are similarities between BDD and requirements as code. Both try to provide a "ubiquitous language" to be spoken by both business people and developers. While the focus of BDD is on specific examples that drive the implementation (and may or may not be executed by automated tests), the focus of requirements as code is on long-term maintenance of requirements in production code. While examples and tests focus on the specific state or behavior resulting from a specific input to the software, the requirements describe the software's behavior in general terms. 
* The Boundary Control Entity Pattern ([BCE](http://epf.eclipse.org/wikis/openup/core.tech.common.extend_supp/guidances/guidelines/entity_control_boundary_pattern_C4047897.html)): The UseCaseModelRunner in requirements as code plays the role of the Control class in BCE, but it is configured by an underlying UseCaseModel.
* Robert C. Martin's [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) and the architectures it is based upon.
