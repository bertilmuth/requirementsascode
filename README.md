# Requirements as code 
[![Gitter](https://badges.gitter.im/requirementsascode/community.svg)](https://gitter.im/requirementsascode/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

![requirements as code logo](./requirementsascode_logo.png)

A *behavior* is configured by a behavior model.

A *behavior model* maps message types to message handlers.

A *message handler* is a function, consumer or supplier of messages.

Your calling code sends all messages to the behavior. The behavior finds the right handler. The handler processes the message, and potentially produces a result.

So the calling code doesn't need to know anything about the internals of your service. It sends all messages to a single behavior instance, and gets a result back. Black box behavior.

Since the behavior is the central point of control for all functions, you can inject and configure the dependencies of all functions through it. That makes it easy to implement a [hexagonal architecture](https://dev.to/bertilmuth/implementing-a-hexagonal-architecture-1kgf) or clean architecture.

This page describes a simple way to get started. Learn how to create a stateless behavior that handles each message individually.

For sequences of interactions, create an actor instead. An actor runs a use case model with flows. It remembers the current position in the flow, and accepts or rejects messages depending on that position. Thus, an actor can serve as an easy to understand alternative to state machines.

See this [wiki page](https://github.com/bertilmuth/requirementsascode/wiki/Actors,-use-cases-and-flows) for an explanation of actors, use cases and flows.

You can find code examples for models with flows [here](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/helloworld).

# Getting started
Requirements as code is available on Maven Central.

The size of the core jar file is around 100 kBytes. It has no further dependencies.

If you are using Maven, include the following in your POM, to use the core:

``` xml
  <dependency>
    <groupId>org.requirementsascode</groupId>
    <artifactId>requirementsascodecore</artifactId>
    <version>2.0</version>
  </dependency>
```

If you are using Gradle, include the following in your build.gradle, to use the core:

```
implementation 'org.requirementsascode:requirementsascodecore:2.0'
```

At least Java 8 is required to use requirements as code, download and install it if necessary.

# How to create a behavior and send messages to it
Let's look at the general steps first.
After that, you'll see a concrete code example.

## Step 1: Create a behavior model
``` java
class MyBehaviorModel implements BehaviorModel{
  @Override
  public Model model() {
    Model model = Model.builder()
      .user(/* command class */).system(/* command handler*/)
      .user(..).system(...)
      ...
    .build();
    return model;
  }
}
...

```
For handling commands, the message handler has a `Consumer<T>` or `Runnable` type, where T is the message class.
For handling queries, use `.systemPublish` instead of `.system`, and the message handler has a `Function<T, U>` type.
For handling events, use `.on()` instead of `.user()`.
For handling exceptions, use the specific exception's class or `Throwable.class` as parameter of `.on()`.

Use `.condition()` before `.user()`/`.on()` to define an additional precondition that must be fulfilled.
You can also use `condition(...)` without `.user()`/`.on()`, meaning: execute at the beginning of the run, or after an interaction, if the condition is fulfilled.
Use `.step(...)` before `.user()`/`.on()` to explicitly name the step - otherwise the steps are named S1, S2, S3...

The order of `user(..).system(...)` statements has no significance here.

## Step 2: Create a behavior based on the model
``` java
BehaviorModel myBehaviorModel = new MyBehaviorModel(...);
Behavior myBehavior = StatelessBehavior.of(myBehaviorModel);
```

## Step 3: Send a message to the behavior
``` java
Optional<T> queryResultOrEvent = myBehavior.reactTo(<Message POJO Object>);
```

Instead of T, use the type you expect to be published. Note that `reactTo()` casts to that type, so if you don't know it, use `Object` for T.
If an unchecked exception is thrown in one of the handler methods, `reactTo()` will rethrow it.
The call to `reactTo()` is synchronous.

# Code example
[Here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloUser.java)'s a behavior with a single interaction.

The user sends a request with the user name ("Joe"). The system says hello ("Hello, Joe.")

``` java
package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.Behavior;
import org.requirementsascode.BehaviorModel;
import org.requirementsascode.Model;
import org.requirementsascode.StatelessBehavior;

public class HelloUser {
  public static void main(String[] args) {
    GreeterModel greeterModel = new GreeterModel(HelloUser::sayHello);
    Behavior greeter = StatelessBehavior.of(greeterModel);
    greeter.reactTo(new SayHelloRequest("Joe"));
  }
  
  private static void sayHello(SayHelloRequest requestsHello) {
    System.out.println("Hello, " + requestsHello.getUserName() + ".");
  }
}

class GreeterModel implements BehaviorModel {
  private final Consumer<SayHelloRequest> sayHello;

  public GreeterModel(Consumer<SayHelloRequest> sayHello) {
    this.sayHello = sayHello;
  }

  @Override
  public Model model() {
    Model model = Model.builder()
      .user(SayHelloRequest.class).system(sayHello)
    .build();
    return model;
  }
}

class SayHelloRequest {
  private final String userName;

  public SayHelloRequest(String userName) {
    this.userName = userName;
  }

  public String getUserName() {
    return userName;
  }
}
```

# Acknowledgements
Requirements as code is influenced by the ideas of [clean architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) and [hexagonal architecture](https://web.archive.org/web/20180822100852/http://alistair.cockburn.us/Hexagonal+architecture). 

It can be used to [implement them](https://dev.to/bertilmuth/implementing-a-hexagonal-architecture-1kgf).

# Further documentation of requirements as code
* [Examples for building/running use case models with flows](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/helloworld)
* [Cross-cutting concerns example](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/crosscuttingconcerns)
* [How to generate documentation from models](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeextract)

# Publications
* [Implementing a hexagonal architecture](https://dev.to/bertilmuth/implementing-a-hexagonal-architecture-1kgf)
* [Kissing the state machine goodbye](https://dev.to/bertilmuth/kissing-the-state-machine-goodbye-34n9)
* [The truth is in the code](https://medium.freecodecamp.org/the-truth-is-in-the-code-86a712362c99)

# Subprojects
* [requirements as code core](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodecore): create and run models. 
* [requirements as code extract](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeextract): generate documentation from the models (or any other textual artifact).
* [requirements as code examples](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples): example projects illustrating the use of requirements as code.

# Build from sources
Use Java >= 11 and the project's gradle wrapper to build from sources.

# Related topics
* The work of Ivar Jacobson on Use Cases. As an example, have a look at [Use Case 2.0](https://www.ivarjacobson.com/publications/white-papers/use-case-ebook).
* The work of Alistair Cockburn on Use Cases, specifically the different goal levels. Look [here](http://alistair.cockburn.us/Use+case+fundamentals) to get started, or read the book "Writing Effective Use Cases".
