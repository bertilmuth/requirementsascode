package org.requirementsascode.testbehavior;

public class TestResponse {
	private final String taskName;

	TestResponse(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskName() {
		return taskName;
	}
}