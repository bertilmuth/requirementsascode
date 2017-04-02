package shoppingappjavafx.gui.controller;

import org.requirementsascode.UseCaseModelRunner;

public abstract class AbstractController {
	private UseCaseModelRunner useCaseRunner;

	public UseCaseModelRunner useCaseRunner() {
		return useCaseRunner;
	}

	public void setUseCaseRunner(UseCaseModelRunner useCaseRunner) {
		this.useCaseRunner = useCaseRunner;
	}
}
