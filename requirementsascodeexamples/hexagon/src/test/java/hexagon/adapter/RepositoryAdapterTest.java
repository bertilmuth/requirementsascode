package hexagon.adapter;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import hexagon.adapter.RepositoryAdapter;

public class RepositoryAdapterTest {
  private RepositoryAdapter repositoryAdapter;

  @Before
  public void setUp() throws Exception {
    this.repositoryAdapter = new RepositoryAdapter();
  }

  @Test
  public void sadPoemContainsWordSad() {
    String poem = repositoryAdapter.getSadPoem();
    assertTrue(poem.contains("sad"));
  }
  
  @Test
  public void happyPoemContainsWordHappy() {
    String poem = repositoryAdapter.getHappyPoem();
    assertTrue(poem.contains("happy"));
  }
  
  @Test
  public void funnyPoemContainsWordFunny() {
    String poem = repositoryAdapter.getFunnyPoem();
    assertTrue(poem.contains("funny"));
  }
}
