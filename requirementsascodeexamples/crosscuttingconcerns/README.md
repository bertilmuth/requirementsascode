# cross-cutting concerns example 01 - measure performance of system reactions
``` java
UseCaseModelBuilder useCaseModelBuilder = UseCaseModelBuilder.newBuilder();
UseCaseModel useCaseModel = 
	useCaseModelBuilder.useCase("Measure performance of simple mathematical operations")
		.basicFlow()
			.step("S1").system(this::addTwoNumbers)
			.step("S2").system(this::calculateTwoToThePowerOfAThousand)
			.step("S3").system(this::displayResults)
	.build();

UseCaseModelRunner useCaseModelRunner = new UseCaseModelRunner();
useCaseModelRunner.adaptSystemReaction(this::measurePerformance);
useCaseModelRunner.run(useCaseModel);
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/crosscuttingconcerns/src/main/java/crosscuttingconcerns/CrossCuttingConcerns01.java).
