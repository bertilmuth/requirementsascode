package shoppingfxexample.gui.controller;

import org.requirementsascode.UseCaseRunner;

public abstract class AbstractUseCaseRunnerController {
	private UseCaseRunner useCaseRunner;

	public UseCaseRunner getUseCaseRunner() {
		return useCaseRunner;
	}

	public void setUseCaseRunner(UseCaseRunner useCaseRunner) {
		this.useCaseRunner = useCaseRunner;
	}
}
