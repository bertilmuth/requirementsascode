package poem.hexagon.internal.domain;

import java.util.List;
import java.util.Random;

/**
 * Picks a random poem from a list of poems.
 * 
 * Inspired by a talk by A. Cockburn and T. Pierrain on hexagonal architecture:
 * https://www.youtube.com/watch?v=th4AgBcrEHA
 * 
 * @author b_muth
 *
 */
public class RandomPoemPicker {
	private Random random;

	public RandomPoemPicker() {
		this.random = new Random();
	}

	/**
	 * Picks a random poem from the specified list of poems.
	 * 
	 * @param poemObjects the poems to pick from
	 * @return a poem from the list, or an empty string if the list is empty
	 */
	public String pickPoem(List<Poem> poemObjects) {
		if (poemObjects.size() == 0) {
			return "";
		}

		int randomIndex = random.nextInt(poemObjects.size());
		String randomPoemText = poemObjects.get(randomIndex).toString();
		return randomPoemText;
	}
}
