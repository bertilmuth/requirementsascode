package shoppingappjavafx.gui.controller;

import org.requirementsascode.ModelRunner;

public abstract class AbstractController {
	private ModelRunner useCaseModelRunner;

	public ModelRunner useCaseModelRunner() {
		return useCaseModelRunner;
	}

	public void setUseCaseModelRunner(ModelRunner useCaseModelRunner) {
		this.useCaseModelRunner = useCaseModelRunner;
	}
}
