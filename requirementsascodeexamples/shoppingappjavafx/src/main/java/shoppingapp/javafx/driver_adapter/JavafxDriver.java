package shoppingapp.javafx.driver_adapter;

import shoppingapp.boundary.Boundary;
import shoppingapp.javafx.driven_adapter.JavafxDisplay;

public class JavafxDriver {
	private Boundary boundary;
	private JavafxDisplay javafxDisplay;

	public JavafxDriver(Boundary boundary, JavafxDisplay javafxDisplay) {
		this.boundary = boundary;
		this.javafxDisplay = javafxDisplay;
	}

	public void reactTo(Object command) {
		javafxDisplay.setJavafxDriver(this);
		boundary.reactTo(command);
	}

	public boolean canReactTo(Class<? extends Object> commandType) {
		return boundary.canReactTo(commandType);
	}
}
