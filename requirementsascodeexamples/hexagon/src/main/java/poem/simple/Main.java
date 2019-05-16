package poem.simple;

import poem.hexagon.boundary.Boundary;
import poem.simple.drivenadapter.ConsolePublisher;
import poem.simple.drivenadapter.PoetryLibrary;
import poem.simple.driveradapter.Driver;

/**
 * Main class that starts the hexagon example application.
 * 
 * Inspired by a talk by A. Cockburn and T. Pierrain on hexagonal architecture:
 * https://www.youtube.com/watch?v=th4AgBcrEHA
 * 
 * @author b_muth
 *
 */
public class Main {
	public static void main(String[] args) {
		new Main().startApplication();
	}

	private void startApplication() {
		// Instantiate driven, right-side adapters
		PoetryLibrary poetryLibrary = new PoetryLibrary();
		ConsolePublisher consolePublisher = new ConsolePublisher();

		// Inject driven adapters into boundary
		Boundary boundary = new Boundary(poetryLibrary, consolePublisher);

		// Start the driver adapter for the application
		new Driver(boundary).run();
	}
}
