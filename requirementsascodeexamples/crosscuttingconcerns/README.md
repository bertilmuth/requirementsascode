# cross-cutting concerns example 01 - measure performance of system reactions
``` java
	public void start() {
		Model model = Model.builder()
			.user(RequestCalculating.class).system(this::calculate)
		.build();

		ModelRunner modelRunner = new ModelRunner();
		modelRunner.handleWith(this::measuresPerformance);
		modelRunner.run(model).reactTo(new RequestCalculating());
	}
```
For the full source code, [look here](https://github.com/bertilmuth/requirementsascode/blob/master/requirementsascodeexamples/crosscuttingconcerns/src/main/java/crosscuttingconcerns/CrossCuttingConcerns01.java).
