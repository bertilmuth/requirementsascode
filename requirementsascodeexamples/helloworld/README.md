# example 01 - system greets user
``` java		
class HelloWorldActor01 extends AbstractActor{
  ...	
  public HelloWorldActor01(Runnable greetsUser) {
    this.greetsUser = greetsUser;
  }

  @Override
  protected Model behavior() {
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
  protected Model behavior() {
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
class HelloWorldActor03 extends AbstractActor {
  ...
  public HelloWorldActor03(Consumer<EnterText> greetsUser) {
    this.greetsUser = greetsUser;
  }

  @Override
  protected Model behavior() {
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

# example 03a - user enters name, system prints it only if calling actor is valid user
``` java
class HelloWorldActor03a extends AbstractActor {
  ...
  public HelloWorldActor03a(Consumer<EnterText> greetsUser) {
    this.greetsUser = greetsUser;
  }

  @Override
  protected Model behavior() {
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
  protected Model behavior() {
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
  protected Model behavior() {
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
class HelloWorldActor06 extends AbstractActor {
  ...
  public HelloWorldActor06(Consumer<EnterText> savesName, Consumer<EnterText> savesAge, Runnable greetsUserWithName,
    Runnable greetsUserWithAge, Condition ageIsOk, Condition ageIsOutOfBounds) {
    this.savesName = savesName;
    this.savesAge = savesAge;
    this.greetsUserWithName = greetsUserWithName;
    this.greetsUserWithAge = greetsUserWithAge;
    this.ageIsOk = ageIsOk;
    this.ageIsOutOfBounds = ageIsOutOfBounds;
  }

  @Override
  protected Model behavior() {
    Model model = Model.builder()
      .useCase("Get greeted")
        .basicFlow()
          .step("S1").as(normalUser).user(entersName).system(savesName)
          .step("S2").as(normalUser, anonymousUser).user(entersAge).system(savesAge)
          .step("S3").as(normalUser).system(greetsUserWithName)
          .step("S4").as(normalUser, anonymousUser).system(greetsUserWithAge)
        .flow("Handle out-of-bounds age").after("S2").condition(ageIsOutOfBounds)
          .step("S3a_1").continuesAt("S2")
        .flow("Handle non-numerical age").anytime()
          .step("S3b_1").on(numberFormatException).continuesAt("S2")
        .flow("Anonymous greeted with age only").after("S2").condition(ageIsOk)
          .step("S3c_1").as(anonymousUser).continuesAt("S4")
        .flow("Anonymous does not enter name").insteadOf("S1")
          .step("S1a_1").as(anonymousUser).user(entersAge).system(savesAge)
          .step("S1a_2").continuesAfter("S2")
      .build();

    return model;
  }

  public void setNormalUser(AbstractActor normalUser) {
    this.normalUser = normalUser;
  }

  public void setAnonymousUser(AbstractActor anonymousUser) {
    this.anonymousUser = anonymousUser;
  }
}
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld06.java).

# example 07 - steps with case condition, model runner moves in if condition false
``` java
class HelloWorldActor07 extends AbstractActor {
  ...
  public HelloWorldActor07(Condition isInputColorRed, Condition isInputColorYellow, Condition isInputColorGreen,
    Runnable setOutputColorToRed, Runnable setOutputColorToYellow, Runnable setOutputColorToGreen, Runnable displayColor) {
    this.isInputColorRed = isInputColorRed;
    this.isInputColorYellow = isInputColorYellow;
    this.isInputColorGreen = isInputColorGreen;
    this.setOutputColorToRed = setOutputColorToRed;
    this.setOutputColorToYellow = setOutputColorToYellow;
    this.setOutputColorToGreen = setOutputColorToGreen;
    this.displayColor = displayColor;
  }
  
  @Override
  protected Model behavior() {
    Model model = Model.builder()
      .useCase("Handle colors")
        .basicFlow()
          .step("S1").inCase(isInputColorRed).system(setOutputColorToRed)
          .step("S2").inCase(isInputColorYellow).system(setOutputColorToYellow)
          .step("S3").inCase(isInputColorGreen).system(setOutputColorToGreen)
          .step("S4").system(displayColor)
        .build();

    return model;
  }
}
```

For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld07.java).