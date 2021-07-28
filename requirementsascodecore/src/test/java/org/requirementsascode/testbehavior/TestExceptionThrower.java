package org.requirementsascode.testbehavior;

import org.requirementsascode.Model;
import org.requirementsascode.BehaviorModel;

public class TestExceptionThrower implements BehaviorModel {
	@Override
	public Model model() {
		return Model.builder()
			.on(String.class).systemPublish(msg -> "Throws an exception, since it's a string (same response type as request type)")
			.build();
	}
}