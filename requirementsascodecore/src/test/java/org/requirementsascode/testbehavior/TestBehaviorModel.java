package org.requirementsascode.testbehavior;

import org.requirementsascode.BehaviorModel;
import org.requirementsascode.Model;

public class TestBehaviorModel implements BehaviorModel {
	@Override
	public Model model() {
		return Model.builder()
			.user(TestCreateListRequest.class).systemPublish(addTask -> new TestCreateListResponse())
			.user(TestAddTaskRequest.class).systemPublish(addTask -> new TestAddTaskResponse())
			.user(TestCompleteTaskRequest.class).system(() -> {})
			.build();
	}

  @Override
  public Object defaultResponse() {
    return null;
  }
}