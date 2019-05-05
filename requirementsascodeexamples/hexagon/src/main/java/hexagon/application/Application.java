package hexagon.application;

import java.util.function.Consumer;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

import hexagon.application.systemreaction.DisplayPoem;
import hexagon.domain.RandomPoemPicker;
import hexagon.port.IObtainPoems;
import hexagon.port.IWriteLines;

public class Application implements Consumer<Object> {

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