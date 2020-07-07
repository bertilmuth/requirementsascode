# requirements as code extract
With requirements as code extract, you can generate plain text documentation 
(e.g HTML pages) from a model inside the code. The model is defined with the requirements as code core project.

## Getting started
If you are using Maven, include the following in your POM:

``` xml
  <dependency>
    <groupId>org.requirementsascode</groupId>
    <artifactId>requirementsascodeextract</artifactId>
    <version>1.6.1</version>
  </dependency>
```

If you are using Gradle, include the following in your build.gradle:

```
implementation 'org.requirementsascode:requirementsascodeextract:1.6.1'
```

This will put the following libraries on the classpath:
* freemarker-2.3.28.jar (FreeMarker)
* commons-lang3-3.5.jar (Apache Commons)
* The current requirements as code extract jar
* The current requirements as code core jar

## Using requirements as code extract
### Build a model
First, you need to build a model using the requirements as code core project:

``` java
Model model = Model.builder()
  .useCase("Use credit card")
    .basicFlow()
    	.step(ASSIGN).user(requestsToAssignLimit).system(assignsLimit)
	.step(WITHDRAW).user(requestsWithdrawal).system(withdraws).reactWhile(accountOpen)
	.step(REPAY).user(requestsRepay).system(repays).reactWhile(accountOpen)
    
    .flow("Withdraw again").after(REPAY)
	.step(WITHDRAW_AGAIN).user(requestsWithdrawal).system(withdraws)
	.step(REPEAT).continuesAt(WITHDRAW)
	    	
    .flow("Cycle is over").anytime()
	.step(CLOSE).on(requestToCloseCycle).system(closesCycle)
	    	
    .flow("Assign limit twice").condition(limitAlreadyAssigned)
	.step(ASSIGN_TWICE).user(requestsToAssignLimit).system(throwsAssignLimitException)
	    	
    .flow("Too many withdrawals").condition(tooManyWithdrawalsInCycle) 
         .step(WITHDRAW_TOO_OFTEN).user(requestsWithdrawal).system(throwsTooManyWithdrawalsException)
 .build();
```

You have to use classes with special names in the model,
as the engine will create documentation from these names.
 
For example, in step `REPAY`, the ```requestsRepay``` constant references the following class:
``` java
public class RequestsRepay {
    private BigDecimal amount;
    public RequestsRepay(BigDecimal amount) {
        this.amount = amount;
    }
    public BigDecimal getAmount() {
        return amount;
    }
}
```

The name of the class needs to be of the form _VerbNoun_, in third person singular.
In the example, it is _RequestsRepay_. 
The documentation created from step ```REPAY``` is: "Repay: As long as account open: User _requests repay_. System repays."

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

### Example document
Here's the full document generated from the above model:

<h1>Use Credit Card</h1>
<h2>Basic flow</h2>
<div></div>
<div><b>Assign limit</b>: User requests to assign limit. System assigns limit.</div>
<div><b>Withdraw</b>: As long as account open: User requests withdrawal. System withdraws.</div>
<div><b>Repay</b>: As long as account open: User requests repay. System repays.</div>
<h2>Withdraw again</h2>
<div>After Repay: </div>
<div><b>Withdraw again</b>: User requests withdrawal. System withdraws.</div>
<div><b>Repeat</b>:  System continues at Withdraw.</div>
<h2>Cycle is over</h2>
<div>Anytime: </div>
<div><b>Close cycle</b>:  Handles RequestToCloseCycle: System closes cycle.</div>
<h2>Assign limit twice</h2>
<div>Anytime, when limit already assigned: </div>
<div><b>Assign limit twice</b>: User requests to assign limit. System throws assign limit exception.</div>
<h2>Too many withdrawals</h2>
<div>Anytime, when too many withdrawals in cycle: </div>
<div><b>Withdraw too often</b>:  System throws too many withdrawals exception.</div>

