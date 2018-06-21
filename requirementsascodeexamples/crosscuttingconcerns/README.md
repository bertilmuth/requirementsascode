# cross-cutting concerns example 01 - measure performance of system reactions
``` java
ModelBuilder modelBuilder = Model.builder();
Model model = 
	modelBuilder.useCase("Measure performance of simple mathematical operations")
		.basicFlow()
			.step("S1").system(this::addsTwoNumbers)
			.step("S2").system(this::calculatesTwoToThePowerOfAThousand)
			.step("S3").system(this::displaysResults)
	.build();

ModelRunner modelRunner = new ModelRunner();
modelRunner.handleWith(this::measuresPerformance);
modelRunner.run(model);
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/crosscuttingconcerns/src/main/java/crosscuttingconcerns/CrossCuttingConcerns01.java).
