package hexagon_example;

import hexagon_example.adapter.driven.ConsolePublisher;
import hexagon_example.adapter.driven.EnglishPoetryLibrary;
import hexagon_example.adapter.driven.GermanPoetryLibrary;
import hexagon_example.adapter.driver.Driver;
import hexagon_example.hexagon.application.Application;
import hexagon_example.hexagon.port.driven.IObtainPoems;
import hexagon_example.hexagon.port.driven.IWriteLines;

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

		// Inject driven adapters into application
		Application application = new Application(englishLibrary, germanLibrary, consolePublisher);
		
		// Start the driver adapter for the application
		Driver driver = new Driver(application);
		driver.run();
	}
}
