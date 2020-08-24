# example 01 - system greets user
``` java		
class HelloWorldActor01 extends AbstractActor{
	...	
  public HelloWorldActor01(Runnable greetsUser) {
    this.greetsUser = greetsUser;
  }

  @Override
  public Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").system(greetsUser)
      .build();

    return model;
  }
}
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld01.java).

# example 02 - system greets user three times
``` java		
class HelloWorldActor02 extends AbstractActor {
  ...
  public HelloWorldActor02(Runnable greetsUser) {
    this.greetsUser = greetsUser;
  }
  
  @Override
  public Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").system(greetsUser).reactWhile(lessThan3)
      .build();

    return model;
  }

  private boolean lessThan3() {
    return greetingsCounter++ < 3;
  }
}
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld02.java).


# example 03 - user enters name, system prints it
``` java
class HelloWorldActor03 extends AbstractHelloWorldExample {
  ...
  public HelloWorldActor03(Consumer<EnterText> greetsUser) {
    this.greetsUser = greetsUser;
  }

  @Override
  public Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").user(entersName).system(greetsUser)
      .build();

    return model;
  }
}
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld03.java).

# example 03a - user enters name, system prints it only if actor is right
``` java
class HelloWorldActor03a extends AbstractActor {
  ...
  public HelloWorldActor03a(Consumer<EnterText> greetsUser) {
    this.greetsUser = greetsUser;
  }

  @Override
  public Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted").as(validUser)
        .basicFlow()
          .step("S1").user(entersName).system(greetsUser)
      .build();

    return model;
  }

  public void setValidUser(AbstractActor validUser) {
    this.validUser = validUser;
  }
}
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld03a.java).


# example 04 - user enters name and age, system prints them (exception thrown if non-numerical age entered)
``` java
class HelloWorldActor04 extends AbstractActor {
  ...
  public HelloWorldActor04(Consumer<EnterText> savesName, Consumer<EnterText> savesAge, Runnable greetsUser) {
    this.savesName = savesName;
    this.savesAge = savesAge;
    this.greetsUser = greetsUser;
  }

  @Override
  public Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").user(entersName).system(savesName)
          .step("S2").user(entersAge).system(savesAge)
          .step("S3").system(greetsUser)
        .build();

    return model;
  }
}

```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld04.java).

# example 05 - user enters name and age, system prints them (with validation)
``` java
class HelloWorldActor05 extends AbstractActor {
  ...
  public HelloWorldActor05(Consumer<EnterText> savesName, Consumer<EnterText> savesAge, Runnable greetsUser,
    Condition ageIsOutOfBounds) {
    this.savesName = savesName;
    this.savesAge = savesAge;
    this.greetsUser = greetsUser;
    this.ageIsOutOfBounds = ageIsOutOfBounds;
  }

  @Override
  public Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").user(entersName).system(savesName)
          .step("S2").user(entersAge).system(savesAge)
          .step("S3").system(greetsUser)
        .flow("Handle out-of-bounds age").insteadOf("S3").condition(ageIsOutOfBounds)
          .step("S3a_1").continuesAt("S2")
        .flow("Handle non-numerical age").after("S2")
          .step("S3b_1").on(numberFormatException).continuesAt("S2")
      .build();

    return model;
  }
}
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld05.java).

# example 06 - user enters name and age as normal user, or only age as anonymous user, system prints the data (with validation)
``` java
  public Model buildModel() {
    ModelBuilder builder = Model.builder();
    normalUser = new Actor("Normal User");
    anonymousUser = new Actor("Anonymous User");
    
    Model model = builder
    .useCase("Get greeted")
      .basicFlow()
        .step("S1").as(normalUser).system(asksForName)
	.step("S2").as(normalUser).user(entersName).system(savesName)
	.step("S3").as(normalUser, anonymousUser).system(asksForAge)
	.step("S4").as(normalUser, anonymousUser).user(entersAge).system(savesAge)
	.step("S5").as(normalUser).system(greetsUserWithName)
	.step("S6").as(normalUser, anonymousUser).system(greetsUserWithAge)
	.step("S7").as(normalUser, anonymousUser).system(stops)
      .flow("Handle out-of-bounds age").insteadOf("S5").condition(ageIsOutOfBounds)
	.step("S5a_1").system(displaysAgeIsOutOfBounds)
	.step("S5a_2").continuesAt("S3")
      .flow("Handle non-numerical age").insteadOf("S5")
	.step("S5b_1").on(numberFormatException).system(displaysAgeIsNonNumerical)
	.step("S5b_2").continuesAt("S3")
      .flow("Anonymous greeted with age only").insteadOf("S5").condition(ageIsOk)
	.step("S5c_1").as(anonymousUser).continuesAt("S6")
      .flow("Anonymous does not enter name").insteadOf("S1")
	.step("S1a_1").as(anonymousUser).continuesAt("S3")
    .build();

  return model;
}
...
Model model = buildModel();
ModelRunner modelRunner = new ModelRunner().as(anonymousUser()).run(model);
while (!systemStopped())
	modelRunner.reactTo(entersText());
exitSystem();
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld06.java).

# example 07 - steps with case condition, model runner moves in if condition false
``` java
public Model buildModel() {
  Model model = Model.builder()
    .useCase("Handle colors")
      .basicFlow()
        .step("S1").inCase(this::isColorRed).system(this::setColorToRed)
        .step("S2").inCase(this::isColorYellow).system(this::setColorToYellow)
        .step("S3").inCase(this::isColorGreen).system(this::setColorToGreen)
        .step("S4").system(this::displayColor)
      .build();
    
  return model;
}
...

Model model = buildModel();
new ModelRunner().run(model);
```

For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld07.java).