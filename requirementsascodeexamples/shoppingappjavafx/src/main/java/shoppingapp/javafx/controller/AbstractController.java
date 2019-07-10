package shoppingapp.javafx.controller;

import shoppingapp.boundary.driver_port.IReactToCommands;

public abstract class AbstractController {
	private IReactToCommands boundary;

	public IReactToCommands boundary() {
		return boundary;
	}

	public void setBoundary(IReactToCommands boundary) {
		this.boundary = boundary;		
	}
}
