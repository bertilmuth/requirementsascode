package hexagonal_architecture.application;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

import hexagonal_architecture.application.systemreaction.DisplayPoem;
import hexagonal_architecture.domain.RandomPoemPicker;
import hexagonal_architecture.port.driven.IObtainPoems;
import hexagonal_architecture.port.driven.IWriteLines;
import hexagonal_architecture.port.driver.IReactToCommands;

/**
 * The application is the only point of communication with left-side driver
 * adapters. It accepts commands, and triggers the appropriate system reaction.
 * 
 * On creation, the application wires up the dependencies between command
 * objects and command handlers, by injecting the command handlers into a use
 * case model. (The command handlers are located in the systemreaction sub
 * package.)
 * 
 * After creation, the application sends each command it receives to the runner
 * of the use case model. The model runner then dispatches the command to the
 * appropriate command handler.
 * 
 * Inspired by a talk by A. Cockburn and T. Pierrain on hexagonal architecture:
 * https://www.youtube.com/watch?v=th4AgBcrEHA
 * 
 * @author b_muth
 *
 */
public class Application implements IReactToCommands {

	private ModelRunner modelRunner;

	public Application(IObtainPoems englishLibrary, IObtainPoems germanLibrary, IWriteLines publishingDevice) {
		Model model = buildModel(englishLibrary, germanLibrary, publishingDevice);
		modelRunner = new ModelRunner().run(model);
	}

	private Model buildModel(IObtainPoems englishLibrary, IObtainPoems germanLibrary, IWriteLines publishingDevice) {
		Runnable displayEnglishPoem = new DisplayPoem(englishLibrary, new RandomPoemPicker(), publishingDevice);
		Runnable displayGermanPoem = new DisplayPoem(germanLibrary, new RandomPoemPicker(), publishingDevice);
		Model model = UseCaseModel.build(displayEnglishPoem, displayGermanPoem);
		return model;
	}

	public void accept(Object commandObject) {
		modelRunner.reactTo(commandObject);
	}
}