package hexagon_example.hexagon.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import hexagon_example.hexagon.domain.RandomPoemPicker;

public class RandomPoemPickerTest {
	private RandomPoemPicker randomPoemPicker;

	@Before
	public void setUp() throws Exception {
		this.randomPoemPicker = new RandomPoemPicker();
	}
	
	@Test
	public void picks_random_poem_from_empty_list() {
		String[] poems = new String[0];		
		
		String randomPoem = randomPoemPicker.pickPoem(poems);
		assertEquals("", randomPoem);
	}
	
	@Test
	public void picks_random_poem_from_single_element_list() {
		String[] poems = new String[] {"Poem1"};		
		
		String randomPoem = randomPoemPicker.pickPoem(poems);
		assertEquals("Poem1", randomPoem);
	}

	@Test
	public void picks_random_poem_from_mutiple_element_list() {
		String[] poems = new String[] {"Poem1", "Poem2", "Poem3"};		
		List<String> poemsList = Arrays.asList(poems);
		
		String randomPoem = randomPoemPicker.pickPoem(poems);
		assertTrue(poemsList.contains(randomPoem));
	}
}
