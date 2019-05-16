package poem.hexagon.internal.domain;

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
	 * @param poems the poems to pick from
	 * @return a poem from the list, or an empty string if the list is empty
	 */
	public String pickPoem(String[] poems) {
		if (poems.length == 0) {
			return "";
		}

		String randomPoem = poems[random.nextInt(poems.length)];
		return randomPoem;
	}
}
