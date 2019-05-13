package hexagonexample;

import hexagonexample.drivenadapter.ConsolePublisher;
import hexagonexample.drivenadapter.EnglishPoetryLibrary;
import hexagonexample.drivenadapter.GermanPoetryLibrary;
import hexagonexample.driveradapter.Driver;
import hexagonexample.hexagon.boundary.Boundary;
import hexagonexample.hexagon.boundary.drivenport.IObtainPoems;
import hexagonexample.hexagon.boundary.drivenport.IWriteLines;
import hexagonexample.hexagon.boundary.driverport.IReactToCommands;

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
