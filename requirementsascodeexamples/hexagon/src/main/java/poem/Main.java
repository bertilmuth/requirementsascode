package poem;

import poem.hexagon.boundary.Boundary;
import poem.hexagon.boundary.drivenport.IObtainPoems;
import poem.hexagon.boundary.drivenport.IWriteLines;
import poem.hexagon.boundary.driverport.IReactToCommands;
import poem.simple.drivenadapter.ConsolePublisher;
import poem.simple.drivenadapter.EnglishPoetryLibrary;
import poem.simple.drivenadapter.GermanPoetryLibrary;
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
		IObtainPoems englishLibrary = new EnglishPoetryLibrary();
		IObtainPoems germanLibrary = new GermanPoetryLibrary();
		IWriteLines consolePublisher = new ConsolePublisher();

		// Inject driven adapters into boundary
		IReactToCommands boundary = new Boundary(englishLibrary, germanLibrary, consolePublisher);
		
		// Start the driver adapter for the application
		new Driver(boundary).run();
	}
}
