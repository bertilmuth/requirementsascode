package poem.hexagon.internal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import poem.hexagon.internal.domain.RandomPoemPicker;

public class RandomPoemPickerTest {
	private RandomPoemPicker randomPoemPicker;

	@Before
	public void setUp() throws Exception {
		this.randomPoemPicker = new RandomPoemPicker();
	}
	
	@Test
	public void picks_random_poem_from_empty_list() {
		List<Poem> poemsList = createPoemList();
		
		String randomPoem = randomPoemPicker.pickPoem(poemsList);
		assertEquals("", randomPoem);
	}
	
	@Test
	public void picks_random_poem_from_single_element_list() {
		List<Poem> poemsList = createPoemList("Poem1");
		
		String randomPoem = randomPoemPicker.pickPoem(poemsList);
		assertEquals("Poem1", randomPoem);
	}

	@Test
	public void picks_random_poem_from_mutiple_element_list() {
		List<Poem> poemsList = createPoemList("Poem1", "Poem2", "Poem3");
		
		String randomPoem = randomPoemPicker.pickPoem(poemsList);
		assertTrue(poemsList.contains(new Poem(randomPoem)));
	}
	
	private List<Poem> createPoemList(String... poems) {
		List<Poem> poemList = Arrays.stream(poems)
				.map(Poem::new)
				.collect(Collectors.toList());
		return poemList;
	}
}
