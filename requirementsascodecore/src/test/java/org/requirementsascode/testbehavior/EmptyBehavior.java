package org.requirementsascode.testbehavior;

import org.requirementsascode.BehaviorModel;
import org.requirementsascode.Model;

public class EmptyBehavior implements BehaviorModel {
	@Override
	public Model model() {
		return Model.builder().build();
	}
}