# cross-cutting concerns example 01 - measure performance of system reactions
``` java
ModelBuilder modelBuilder = Model.builder();
Model model = 
	modelBuilder.useCase("Measure performance of simple mathematical operations")
		.basicFlow()
			.step("S1").system(this::addTwoNumbers)
			.step("S2").system(this::calculateTwoToThePowerOfAThousand)
			.step("S3").system(this::displayResults)
	.build();

ModelRunner modelRunner = new ModelRunner();
modelRunner.adaptSystemReaction(this::measurePerformance);
modelRunner.run(model);
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/crosscuttingconcerns/src/main/java/crosscuttingconcerns/CrossCuttingConcerns01.java).
