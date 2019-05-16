package poem.hexagon.internal.commandhandler;

import java.util.function.Consumer;

import poem.hexagon.boundary.AskForPoem;
import poem.hexagon.boundary.drivenport.IObtainPoems;
import poem.hexagon.boundary.drivenport.IWriteLines;
import poem.hexagon.internal.domain.RandomPoemPicker;

/**
 * The command handler for displaying a random poem.
 * 
 * Inspired by a talk by A. Cockburn and T. Pierrain on hexagonal
 * architecture: https://www.youtube.com/watch?v=th4AgBcrEHA
 * 
 * @author b_muth
 *
 */
public class DisplayRandomPoem implements Consumer<AskForPoem> {
	private IObtainPoems poemObtainer;
	private RandomPoemPicker randomPoemPicker;
	private IWriteLines lineWriter;

	public DisplayRandomPoem(IObtainPoems poemObtainer, IWriteLines lineWriter) {
		this.poemObtainer = poemObtainer;
		this.randomPoemPicker = new RandomPoemPicker();
		this.lineWriter = lineWriter;
	}

	@Override
	public void accept(AskForPoem askForPoem) {
		String[] poems = obtainPoems(askForPoem);
		String poem = pickRandomPoem(poems);
		displayPoem(poem);		
	}

	private String[] obtainPoems(AskForPoem askForPoem) {
		String language = askForPoem.getLanguage();
		String[] poems = poemObtainer.getMePoems(language);
		return poems;
	}
	
	private String pickRandomPoem(String[] poems) {
		return randomPoemPicker.pickPoem(poems);
	}
	
	private void displayPoem(String poem) {
		lineWriter.writeLines(poem);
	}
}
