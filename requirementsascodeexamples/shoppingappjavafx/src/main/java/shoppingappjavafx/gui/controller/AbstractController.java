package shoppingappjavafx.gui.controller;

import org.requirementsascode.UseCaseRunner;

public abstract class AbstractController {
	private UseCaseRunner useCaseRunner;

	public UseCaseRunner useCaseRunner() {
		return useCaseRunner;
	}

	public void setUseCaseRunner(UseCaseRunner useCaseRunner) {
		this.useCaseRunner = useCaseRunner;
	}
}
