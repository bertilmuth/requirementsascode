# requirementsascodeextract
With requirementsascodeextract, you can generate plain text documentation (e.g HTML pages)
from a use case model inside the code.

## Getting started
In order to use requirementsascodeextract, you need the following libraries on the classpath:
* freemarker-2.3.26-incubating.jar (FreeMarker)
* commons-lang3-3.5.jar (Apache Commons)
* The current requirementsascodeextract.jar
* The current requirementsascodecore.jar

If you don't want to download them manually, and you don't use a build system like gradle,
you can download the shoppingappextract jar from the Releases tab. 

## Using requirementsascodeextract
In order to use requirementsascodeextract, you need to:
* Obtain a use case model builder, and build a model with it
* Use a template engine to extract the use cases, and generate documentation

### Obtain a use case model builder, and build a model with it
Here's how you build a use case model from scratch. 
Note: this model does not make too much sense. It is just an example.

``` java
UseCaseModel useCaseModel = 
  UseCaseModelBuilder.newBuilder()
	.useCase("Get greeted")
		.basicFlow()
			.step("S1").system(promptUserToEnterName())
			.step("S2").user(enterName()).system(greetUser())
			.step("S3").user(decideToQuit())
			.step("S4").system(quit())
		.flow("Alternative Flow A").insteadOf("S4")
			.step("S4a_1").system(blowUp())
			.step("S4a_2").continueAt("S1")
		.flow("Alternative Flow B").after("S3")
			.step("S4b_1").continueAfter("S2")
		.flow("Alternative Flow C").when(thereIsNoAlternative())
			.step("S5").continueWithoutAlternativeAt("S4")
		.flow("Alternative Flow D").insteadOf("S4").when(thereIsNoAlternative())
			.step("S6").continueAt("S1")
.build();
```

You have to use classes with special names in the model,
as the engine will create documentation from these names.
 
For example, in step S2, the ```enterName()``` method returns the following class:
``` java
public class EnterName {
	public final String name;
	
	public EnterName(String name) {
		this.name = name;
	}
}
```

The name of the class needs to be of the form _VerbNoun_, in first person singular.
In the example, it is _EnterName_. 
The documentation created from step S2 will read: "S2. User _enters name_. System greets user."

### Use a template engine to extract the use cases, and generate documentation
You can create an engine to extract the use cases like this:
``` java
FreeMarkerEngine engine = new FreeMarkerEngine(basePackagePath);
```

For ```basePackagePath```, you specify your own package path in your classpath, where your FreeMarker templates are located. For example, if you use standard ```src/main/resources``` or ```src/test/resources``` folders,
this could be the package path below that folder. 

You can extract the use cases with this call:
``` java
engine.extract(useCaseModel, templateFileName, outputWriter);
```

The first parameter is the use case model, as shown above.
The second parameter is the name of the template file, relative to the base package path (during construction).
The third parameter is a ```java.io.Writer``` that produces the output text.

Here's an example FreeMarker template file:
``` 
<#include "./lib/extract.ftl"/>
<html>
<head>
  <title>Requirements as Code - Extract Example</title>
</head>
<body>
  	<#list useCaseModel.useCases as useCase>
  		<h1>Use Case: ${useCase?capitalize}</h1>
		<#list useCase.flows as useCaseFlow>
	  		<h2>${useCaseFlow?capitalize}</h2>
	  		<div><@flowPredicate f=useCaseFlow/></div>
			<#list useCaseFlow.steps as step>
				<div>${step}: <@reactWhile s=step/><@userStep s=step/><@systemStep s=step/></div>
			</#list>
		</#list>
  	</#list>
</body>
</html>
```

The first line includes some useful macros and functions to make extracting easy.
In the example, the ```extract.ftl``` template containing these macros must be in the ```lib``` subfolder.

Then, you start with the ```useCaseModel``` instance provided by the engine to iterate over the model.

See the [FreeMarker](http://freemarker.org/docs/dgui.html) documentation for details.
See this [test class](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeextract/src/test/java/org/requirementsascode/extract/freemarker/FreemarkerEngineTest.java) for details on how to use requirementsascodeextract.
