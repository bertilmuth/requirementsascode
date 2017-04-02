package shoppingappjavafx.gui.controller;

import org.requirementsascode.UseCaseModelRunner;

public abstract class AbstractController {
	private UseCaseModelRunner useCaseModelRunner;

	public UseCaseModelRunner useCaseModelRunner() {
		return useCaseModelRunner;
	}

	public void setUseCaseModelRunner(UseCaseModelRunner useCaseModelRunner) {
		this.useCaseModelRunner = useCaseModelRunner;
	}
}
