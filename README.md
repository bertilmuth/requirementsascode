# requirements as code 
[![Build Status](https://travis-ci.org/bertilmuth/requirementsascode.svg?branch=master)](https://travis-ci.org/bertilmuth/requirementsascode)

![requirements as code logo](./requirementsascode_logo.png)

Requirements as code does not replace conversations with stakeholders. It's a formal way to record conversation results where it matters: in the code. Translate use cases into code to improve the long term maintenance of your application.
 
In use case theory, use cases look at the system from a user's perspective. Technology decisions are postponed to the use case realization. This separation enables more focused discussions about the value the system provides to users, and finding the right solution for the problem to solve.

In requirements as code, use case models are free of technical concerns as well. Models call message handlers through interfaces. Message handlers are the use case realization. They orchestrate the calls to the domain code and to the infrastructure outside of the boundary. By switching method handlers, or by injecting different dependencies into them, you can switch your application's technical infrastructure.

In use case theory, a use case specifies interactions between users and the system.

In requirements as code, an interaction is:
* a message class, and
* a message handler that defines how the system reacts when receiving an instance of the message class. 

The message handler calls the domain/infrastructure code and then:
* doesn't return anything,
* returns a query result, or 
* returns an event to be published.

You can also specify a precondition. 

If your interactions are independent from each other, that's your use case model. For sequences of interactions, create a [use case model with flows](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/helloworld) instead. 
It's a simple alternative to state machines.

# influences and special features
Requirements as code is influenced by the ideas of [clean architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) and [hexagonal architecture](https://web.archive.org/web/20180822100852/http://alistair.cockburn.us/Hexagonal+architecture). It can be used to [implement them](https://dev.to/bertilmuth/implementing-a-hexagonal-architecture-1kgf). And it goes beyond them.

When you follow the design principles of requirements as code, you will end up with pure domain code. The domain code doesn't communicate with technical infrastructure, not even through interfaces. That's why you can test the domain code without mocking anything. That's similar to the idea of a [functional core and an imperative shell](https://www.destroyallsoftware.com/screencasts/catalog/functional-core-imperative-shell).

The use case model at the boundary represents the single source of truth for interactions started by the user. You can [generate living documentation](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeextract) from the use case model. The generated use case documents represent always up to date information about how the system works from a user's perspective.

And since [use case model with flows](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/helloworld) are processes, you can use this library to implement [process managers](https://www.enterpriseintegrationpatterns.com/patterns/messaging/ProcessManager.html) as well.

# getting started
Requirements as code is available on Maven Central.

The size of the core jar file is less than 64 kBytes. It has no further dependencies.

If you are using Maven, include the following in your POM, to use the core:

``` xml
  <dependency>
    <groupId>org.requirementsascode</groupId>
    <artifactId>requirementsascodecore</artifactId>
    <version>1.3</version>
  </dependency>
```

If you are using Gradle, include the following in your build.gradle, to use the core:

```
implementation 'org.requirementsascode:requirementsascodecore:1.3'
```

At least Java 8 is required to use requirements as code, download and install it if necessary.

# how to build and run a use case model
Let's look at the general steps to building and running a model first.
After that, you'll see a concrete code example.

## Step 1: Build a use case model
``` java
Model model = Model.builder()
	.user(/* message class */).system(/* message handler*/)
	.user(..).system(...)
	...
.build();
```

For handling commands, the message handler has a `Consumer<X>` or `Runnable` type, where X is the message class.
For handling queries or publishing events, the message handler has a `Function<X, Object>` type.
For handling events, use `.on()` instead of `.user()`.
For handling exceptions, use the specific exception's class or `Throwable.class` as parameter of `.on()`.
Use `.condition()` before `.user()`/`.on()` to define an additional precondition that must be fulfilled.
You can also use `condition(...)` without `.user()`/`.on()`, meaning: execute at the beginning of the run, or after an interaction, if the condition is fulfilled.

The order of `user(..).system(...)` statements has no significance here.

## Step 2: Create a runner, and run the model
``` java
ModelRunner runner = new ModelRunner().run(model);
```

## Step 3: Send a message to the runner
``` java
Optional<Object> queryResultOrEvent = runner.reactTo(<Message POJO Object>);
```
To customize the behavior when the runner reacts to a message, use `modelRunner.handleWith()` (example [here](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/crosscuttingconcerns)).

By default, if a message's class is not declared in the model, the runner consumes it silently.
To customize that behavior, use `modelRunner.handleUnhandledWith()`. 
If an unchecked exception is thrown in one of the handler methods and it is not handled by any 
other handler method, the runner will rethrow it.

# Example for building and running a use case model
There's a single use case with a single interaction.

The user sends a request with the user name ("Joe"). The system says hello ("Hello, Joe.")

``` java
package hello;

import java.util.function.Consumer;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

public class HelloUser {
  public static void main(String[] args) {
    Model model = new ModelBuilder().build(HelloUser::sayHello);
    ModelRunner modelRunner = new ModelRunner().run(model);
    modelRunner.reactTo(new RequestHello("Joe"));
  }
  
  public static void sayHello(RequestHello requestHello) {
    System.out.println("Hello, " + requestHello.getUserName() + ".");
  }
}

class ModelBuilder {
  private static final Class<RequestHello> requestsHello = RequestHello.class;

  public Model build(Consumer<RequestHello> saysHello) {
    Model model = Model.builder()
      .user(requestsHello).system(saysHello)
     .build();
    return model;
  }
}

class RequestHello {
  private String userName;

  public RequestHello(String userName) {
    this.userName = userName;
  }

  public String getUserName() {
    return userName;
  }
}
```

# Example for applying the design principles
The example above has shown how to build and run a use case model. In practice, that already gives you the benefit of recording the interaction in the code for long term maintenance.
To apply the requirements as code design principles, to clearly separate requirements from realization and get to a pure domain model, the above example needs to change as follows.

## Boundary
There needs to be a boundary that creates and runs the use case model, and reacts to messages.
It gets the message handlers injected into its constructor as interfaces, and builds the model based on them.
The boundary is implemented as a function that transforms an input (i.e. the message) to an output (the optional query result or published event). That's why the `Boundary` class implements `Function<Object, Optional<Object>>`.

``` java
class Boundary implements Function<Object, Optional<Object>> {
  private static final Class<RequestHello> requestsHello = RequestHello.class;
  private Model model;

  /**
   * The constructor that builds the model by using the injected message handlers.
   * 
   * @param saysHello the message handler for saying hello to the user
   */
  public Boundary(Consumer<RequestHello> saysHello) {
    buildModel(saysHello);
  }

  private void buildModel(Consumer<RequestHello> saysHello) {
    model = Model.builder()
      .user(requestsHello).system(saysHello)
     .build();
  }

  /**
   * Reacts to the specified message by dispatching it to a message handler.
   * 
   * @param message the message to dispatch
   * @return the result of a query, a published event, or an empty Optional.
   */
  public Optional<Object> apply(Object message) {
    return new ModelRunner().run(model).reactTo(message);
  }
}
```

## Message senders
There needs to be someone outside of the boundary who's sending messages to the boundary.
In practice, this could be a Spring Controller, or a desktop GUI, for example.
The message sender gets the boundary injected into its constructor as interface.
After that, it can send messages to the boundary.

``` java
class MessageSender {
  private Function<Object, Optional<Object>> boundary;

  public MessageSender(Function<Object, Optional<Object>> boundary) {
    this.boundary = boundary;
  }

  /**
   * Send messages to the boundary. In this example, we don't care 
   * about the return value of the call, because we don't send a query
   * or publish events.
   */
  public void sendMessages() {
    boundary.apply(new RequestHello("Joe"));
  }
}
```

## Messages
Messages should be simple and immutable POJOs. 
They just carry the information needed to be processed by the message handler.
No domain logic is allowed here.
In the example, the `RequestHello` class represents a command that carries the user name.

``` java
class RequestHello {
  private String userName;

  public RequestHello(String userName) {
    this.userName = userName;
  }

  public String getUserName() {
    return userName;
  }
}
```

## Message handlers
Message handlers orchestrate the calls to the infrastructure and domain code.
They are 'dumb' in the sense that they don't contain business logic themselves.

``` java
class SayHello implements Consumer<RequestHello> {
  private MessageGenerator messageGenerator;
  private OutputAdapter outputAdapter;

  public SayHello() {
    this.messageGenerator = new MessageGenerator();
    this.outputAdapter = new OutputAdapter();
  }
  
  public void accept(RequestHello requestHello) {
    String message = messageGenerator.userNameToMessage(requestHello.getUserName());
    outputAdapter.showMessage(message);
  }
}
```

## Infrastructure classes
These are classes that connect to external services or the infrastructure.
In the example, this is the class that prints the message to the console.

``` java
class OutputAdapter{
  public void showMessage(String message) {
    System.out.println(message);
  }
}
```

## Pure domain code
These are the domain classes. They are side effect free, since all communication with the infrastructure happens in the message handler.

In the example, there is only a single domain function: taking the user name and converting it to a message to display.

``` java
class MessageGenerator{
  public String userNameToMessage(String userName) {
    return "Hello, " + userName + ".";
  }
}
```

# Complete example code for applying the design priciples
Here's the complete example as a single file for convenience.

``` java
package hello;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

public class Main {
  public static void main(String[] args) {
    Boundary boundary = new Boundary(new DisplayHello());
    new MessageSender(boundary).sendMessages();
  }
}

/**
 * Boundary class that builds and runs the use case model, and reacts to messages by
 * dispatching them to message handlers.
 */
class Boundary implements Function<Object, Optional<Object>> {
  private static final Class<RequestHello> requestsHello = RequestHello.class;
  private Model model;

  /**
   * The constructor that builds the model by using the injected message handlers.
   * 
   * @param saysHello the message handler for saying hello to the user
   */
  public Boundary(Consumer<RequestHello> saysHello) {
    buildModel(saysHello);
  }

  private void buildModel(Consumer<RequestHello> saysHello) {
    model = Model.builder()
      .user(requestsHello).system(saysHello)
     .build();
  }

  /**
   * Reacts to the specified message by dispatching it to a message handler.
   * 
   * @param message the message to dispatch
   * @return the result of a query, a published event, or an empty Optional.
   */
  public Optional<Object> apply(Object message) {
    return new ModelRunner().run(model).reactTo(message);
  }
}

/**
 * Sender of the message, external to the boundary
 */
class MessageSender {
  private Function<Object, Optional<Object>> boundary;

  public MessageSender(Function<Object, Optional<Object>> boundary) {
    this.boundary = boundary;
  }

  /**
   * Send messages to the boundary. In this example, we don't care 
   * about the return value of the call, because we don't send a query
   * or publish events.
   */
  public void sendMessages() {
    boundary.apply(new RequestHello("Joe"));
  }
}

/**
 * Command class
 */
class RequestHello {
  private String userName;

  public RequestHello(String userName) {
    this.userName = userName;
  }

  public String getUserName() {
    return userName;
  }
}

/**
 * Message handlers
 */
class SayHello implements Consumer<RequestHello> {
  private MessageGenerator messageGenerator;
  private OutputAdapter outputAdapter;

  public SayHello() {
    this.messageGenerator = new MessageGenerator();
    this.outputAdapter = new OutputAdapter();
  }
  
  public void accept(RequestHello requestHello) {
    String message = messageGenerator.userNameToMessage(requestHello.getUserName());
    outputAdapter.showMessage(message);
  }
}

/**
 * Infrastructure classes
 */
class OutputAdapter{
  public void showMessage(String message) {
    System.out.println(message);
  }
}

/**
 * Domain classes
 */
class MessageGenerator{
  public String userNameToMessage(String userName) {
    return "Hello, " + userName + ".";
  }
}
```

# publishing events
When you use the `system()` method, you are restricted to just consuming messages.
But you can also publish events with `systemPublish()`, like so:

``` java
private void buildModel() {
  Model model = Model.builder()
    .on(EnterName.class).systemPublish(this::publishNameAsString) 
    .on(String.class).system(this::displayNameString) 
   .build();			
}

private String publishNameAsString(EnterName enterName) {
  return enterName.getUserName();
}

public void displayNameString(String nameString) {
  System.out.println("Welcome, " + nameString + ".");
}
```

As you can see, `publishNameAsString()` takes a command object as input parameter, and returns an event to be published. In this case, a String.
By default, the model runner takes the returned event and publishes it to the model. 

This behavior can be overriden by specifying a custom event handler on the ModelRunner with `publishWith()`.
For example, you can use `modelRunner.publishWith(queue::put)` to publish events to an event queue.

# documentation of requirements as code
* [Examples for building/running state based use case models](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/helloworld)
* [How to generate documentation from models](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeextract)
* [Cross-cutting concerns example](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/crosscuttingconcerns)

# publications
* [Implementing a hexagonal architecture](https://dev.to/bertilmuth/implementing-a-hexagonal-architecture-1kgf)
* [Kissing the state machine goodbye](https://dev.to/bertilmuth/kissing-the-state-machine-goodbye-34n9)
* [The truth is in the code](https://medium.freecodecamp.org/the-truth-is-in-the-code-86a712362c99)

# subprojects
* [requirements as code core](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodecore): create and run models. 
* [requirements as code extract](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeextract): generate documentation from the models (or any other textual artifact).
* [requirements as code examples](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples): example projects illustrating the use of requirements as code.

# build from sources
Use Java >=11 and the project's gradle wrapper to build from sources.

# related topics
* The work of Ivar Jacobson on Use Cases. As an example, have a look at [Use Case 2.0](https://www.ivarjacobson.com/publications/white-papers/use-case-ebook).
* The work of Alistair Cockburn on Use Cases, specifically the different goal levels. Look [here](http://alistair.cockburn.us/Use+case+fundamentals) to get started, or read the book "Writing Effective Use Cases".
