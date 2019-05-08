package hexagon_example.hexagon.boundary;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

import hexagon_example.hexagon.boundary.driven_port.IObtainPoems;
import hexagon_example.hexagon.boundary.driven_port.IWriteLines;
import hexagon_example.hexagon.boundary.driver_port.IReactToCommands;
import hexagon_example.hexagon.internal.domain.RandomPoemPicker;
import hexagon_example.hexagon.internal.system_reaction.DisplayPoem;

/**
 * The boundary class is the only point of communication with left-side driver
 * adapters. It accepts commands, and triggers the appropriate system reaction.
 * 
 * On creation, this class wires up the dependencies between command objects and
 * command handlers, by injecting the command handlers into a use case model.
 * (The command handlers are located in the system_reaction package.)
 * 
 * After creation, this class sends each command it receives to the runner
 * of the use case model. The model runner then dispatches the command to the
 * appropriate command handler, which in turn calls the driven adapters.
 * 
 * Inspired by a talk by A. Cockburn and T. Pierrain on hexagonal architecture:
 * https://www.youtube.com/watch?v=th4AgBcrEHA
 * 
 * @author b_muth
 *
 */
public class Boundary implements IReactToCommands {

	private ModelRunner modelRunner;

	public Boundary(IObtainPoems englishLibrary, IObtainPoems germanLibrary, IWriteLines publishingDevice) {
		Model model = buildModel(englishLibrary, germanLibrary, publishingDevice);
		modelRunner = new ModelRunner().run(model);
	}

	private Model buildModel(IObtainPoems englishLibrary, IObtainPoems germanLibrary, IWriteLines publishingDevice) {
		Runnable displayEnglishPoem = new DisplayPoem(englishLibrary, new RandomPoemPicker(), publishingDevice);
		Runnable displayGermanPoem = new DisplayPoem(germanLibrary, new RandomPoemPicker(), publishingDevice);
		Model model = UseCaseModel.build(displayEnglishPoem, displayGermanPoem);
		return model;
	}

	public void reactTo(Object commandObject) {
		modelRunner.reactTo(commandObject);
	}
}