package poem.hexagon.internal.commandhandler;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import poem.hexagon.boundary.command.AskForPoem;
import poem.hexagon.boundary.drivenport.IObtainPoems;
import poem.hexagon.boundary.drivenport.IWriteLines;
import poem.hexagon.internal.domain.Poem;
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
		List<Poem> poems = obtainPoems(askForPoem);
		Optional<Poem> poem = pickRandomPoem(poems);
		displayPoem(poem);		
	}

	private List<Poem> obtainPoems(AskForPoem askForPoem) {
		String language = askForPoem.getLanguage();
		String[] poems = poemObtainer.getMePoems(language);
		List<Poem> poemDomainObjects = 
			Arrays.stream(poems)
				.map(Poem::new)
				.collect(Collectors.toList());
		return poemDomainObjects;
	}
	
	private Optional<Poem> pickRandomPoem(List<Poem> poemList) {
		Optional<Poem> randomPoem = randomPoemPicker.pickPoem(poemList);
		return randomPoem;
	}
	
	private void displayPoem(Optional<Poem> poem) {
		poem.ifPresent(p -> lineWriter.writeLines(p.toString()));
	}
}
