# example 01 - system prints 'Hello, User.'
``` java		
public Model buildModel() {
  Model model = Model.builder()
    .useCase("Get greeted")
      .basicFlow()
        .step("S1").system(greetsUser)
    .build();

  return model;
}
...
Model model = buildModel();
new ModelRunner().run(model);
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld01.java).

# example 02 - system prints 'Hello, User.' three times
``` java		
public Model buildModel() {
  Model model = Model.builder()
    .useCase("Get greeted")
      .basicFlow()
        .step("S1").system(greetsUser).reactWhile(lessThan3)
    .build();

  return model;
}
...
Model model = buildModel();
new ModelRunner().run(model);
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld02.java).


# example 03 - user enters name, system prints it
``` java
public Model buildModel() {
  Model model = Model.builder()
    .useCase("Get greeted")
      .basicFlow()
        .step("S1").system(asksForName)
	.step("S2").user(entersName).system(greetsUser)
    .build();

  return model;
}
...
Model model = buildModel();
new ModelRunner().run(model).reactTo(entersText());
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld03.java).

# example 03a - user enters first name, system prints it only if actor is right
``` java
public Model buildModel() {
  ModelBuilder builder = Model.builder();
  validUser = builder.actor("Valid User");
  invalidUser = builder.actor("Invalid User");
  
  Model model = builder
    .useCase("Get greeted").as(validUser)
      .basicFlow()
        .step("S1").system(asksForName)
	.step("S2").user(entersName).system(greetsUser)
    .build();

  return model;
}
...
Model model = buildModel();
ModelRunner modelRunner = new ModelRunner().run(model);

// The next command will not be handled, because the actor is wrong
modelRunner.as(invalidUser).reactTo(new EnterText("Ignored Command"));

// This command will be handled
modelRunner.as(validUser).reactTo(entersText());
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld03a.java).


# example 04 - user enters name and age, system prints them (exception thrown if non-numerical age entered)
``` java
public Model buildModel() {
  Model model = Model.builder()
    .useCase("Get greeted")
      .basicFlow()
        .step("S1").system(asksForName)
	.step("S2").user(entersName).system(savesName)
	.step("S3").system(asksForAge)
	.step("S4").user(entersAge).system(savesAge)
	.step("S5").system(greetsUser)
    .build();
  
  return model;
}
...
Model model = buildModel();
ModelRunner modelRunner = new ModelRunner().run(model);
modelRunner.reactTo(entersText());
modelRunner.reactTo(entersText());
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld04.java).

# example 05 - user enters name and age, system prints them (with validation)
``` java
public Model buildModel() {
  Model model = Model.builder()
    .useCase("Get greeted")
      .basicFlow()
        .step("S1").system(asksForName)
	.step("S2").user(entersName).system(savesName)
	.step("S3").system(asksForAge)
	.step("S4").user(entersAge).system(savesAge)
	.step("S5").system(greetsUser)
	.step("S6").system(stops)
      .flow("Handle out-of-bounds age").insteadOf("S5").condition(ageIsOutOfBounds)
        .step("S5a_1").system(displaysAgeIsOutOfBounds)
	.step("S5a_2").continuesAt("S3")
      .flow("Handle non-numerical age").insteadOf("S5")
	.step("S5b_1").on(numberFormatException).system(displaysAgeIsNonNumerical)
	.step("S5b_2").continuesAt("S3")
    .build();

  return model;
}
...
Model model = buildModel();
ModelRunner modelRunner = new ModelRunner().run(model);
while (!systemStopped())
  modelRunner.reactTo(entersText());
exitSystem();
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/helloworld/src/main/java/helloworld/HelloWorld05.java).

# example 06 - user enters name and age as normal user, or only age as anonymous user, system prints the data (with validation)
``` java
  public Model buildModel() {
    ModelBuilder builder = Model.builder();
    normalUser = builder.actor("Normal User");
    anonymousUser = builder.actor("Anonymous User");
    
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
