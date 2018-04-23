package shoppingappjavafx.gui.controller;

import org.requirementsascode.ModelRunner;

public abstract class AbstractController {
	private ModelRunner modelRunner;

	public ModelRunner modelRunner() {
		return modelRunner;
	}

	public void setModelRunner(ModelRunner modelRunner) {
		this.modelRunner = modelRunner;
	}
}
