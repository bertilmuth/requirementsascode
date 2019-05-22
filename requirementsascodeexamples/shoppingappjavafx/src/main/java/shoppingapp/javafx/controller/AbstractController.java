package shoppingapp.javafx.controller;

import shoppingapp.javafx.driver_adapter.JavafxDriver;

public abstract class AbstractController {
	private JavafxDriver javafxDriver;

	public JavafxDriver javafxDriver() {
		return javafxDriver;
	}

	public void setJavafxDriver(JavafxDriver javafxDriver) {
		this.javafxDriver = javafxDriver;		
	}
}
