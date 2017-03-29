# cross-cutting concerns example 01 - measure performance of system reactions
``` java
UseCaseRunner useCaseRunner = new UseCaseRunner();
useCaseRunner.adaptSystemReaction(this::measurePerformance);

useCaseRunner.useCaseModel().useCase("Measure performance of simple mathematical operations")
	.basicFlow()
		.step("S1").system(addTwoNumbers())
		.step("S2").system(calculateTwoToThePowerOfAThousand())
		.step("S3").system(displayResults());

useCaseRunner.run();
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/crosscuttingconcerns/src/main/java/crosscuttingconcerns/CrossCuttingConcerns01_MeasurePerformanceExample.java).