# requirements as code extract
With requirements as code extract, you can generate plain text documentation 
(e.g HTML pages) from a model inside the code. 

The model is defined with the requirements as code core project.

## Getting started
If you are using Maven, include the following in your POM:

``` xml
  <dependency>
    <groupId>org.requirementsascode</groupId>
    <artifactId>requirementsascodeextract</artifactId>
    <version>0.8.2</version>
  </dependency>
```

If you are using Gradle, include the following in your build.gradle:

```
compile 'org.requirementsascode:requirementsascodeextract:0.8.2'
```

This will put the following libraries on the classpath:
* freemarker-2.3.26-incubating.jar (FreeMarker)
* commons-lang3-3.5.jar (Apache Commons)
* The current requirements as code extract jar
* The current requirements as code core jar

## Using requirements as code extract
### Build a model
Here's how you build a model from scratch. 
Note: this model does not make too much sense. It is just a comprehensive example.

``` java
Model model = modelBuilder
    .useCase("Included use case")
      .basicFlow()
        .step("Included step").system(new IgnoreIt<>())
    .useCase("Get greeted")
      .basicFlow()
        .step("S1").system(promptsUserToEnterName())
        .step("S2").user(entersName()).system(greetsUser())
        .step("S3").as(firstActor).user(entersName()).system(greetsUser()).reactWhile(someConditionIsFulfilled())
        .step("S4").as(firstActor, secondActor).user(decidesToQuit())
        .step("S5").as(firstActor, secondActor).system(promptsUserToEnterName())
        .step("S6").system(quits())
      .flow("Alternative flow A").insteadOf("S4")
        .step("S4a_1").system(blowsUp())
        .step("S4a_2").continuesAt("S1")
      .flow("Alternative flow B").after("S3")
        .step("S4b_1").continuesAfter("S2")
      .flow("Alternative flow C").when(thereIsNoAlternative())
        .step("S5a").continuesWithoutAlternativeAt("S4")
      .flow("Alternative flow D").insteadOf("S4").when(thereIsNoAlternative())
        .step("S4c_1").includesUseCase("Included use case")
        .step("S4c_2").continuesAt("S1")
      .flow("EX").anytime()
      	.step("EX1").handles(Exception.class).system(logsException())
    .build();  
```

You have to use classes with special names in the model,
as the engine will create documentation from these names.
 
For example, in step S2, the ```entersName()``` method returns the following class:
``` java
public class EntersName {
	public final String name;
	
	public EntersName(String name) {
		this.name = name;
	}
}
```

The name of the class needs to be of the form _VerbNoun_, in third person singular.
In the example, it is _EntersName_. 
The documentation created from step S2 will read: "S2. User _enters name_.System greets user."

### Use template engine to generate documentation
You can create the engine like this:

``` java
FreeMarkerEngine engine = new FreeMarkerEngine(basePackagePath);
```

For ```basePackagePath```, you specify your own package path in your classpath, where your FreeMarker templates are located. For example, if you use standard ```src/main/resources``` or ```src/test/resources``` folders,
this is the package path below that folder. 

You can generate the documentation with this call:
``` java
engine.extract(model, templateFileName, outputWriter);
```

The first parameter is the model, as shown above.
The second parameter is the name of the template file, relative to the base package path (during construction).
The third parameter is a ```java.io.Writer``` that produces the output text.

Here's an example FreeMarker template file:

``` xml
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
  <title>Requirements as Code - Extract Example</title>
</head>
<body>
  	<#list model.useCases as useCase>
  		<h1>Use Case: ${useCase?capitalize}</h1>
		<#list useCase.flows as f>
	  		<h2>${f}</h2>
	  		<div>${flowCondition(f)}</div>
			<#list f.steps as s>
				<div>${s}. ${reactWhileOfStep(s)}${actorPartOfStep(s)}${userPartOfStep(s)}${systemPartOfStep(s)}</div>
			</#list>
		</#list>
  	</#list>
</body>
</html>
```

The template file starts with the ```model``` instance provided by the engine, then it iterates over the model.

See the [FreeMarker](http://freemarker.org/docs/dgui.html) documentation for details.
See this [test class](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeextract/src/test/java/org/requirementsascode/extract/freemarker/FreemarkerEngineTest.java) for details on how to use requirements as code extract.
