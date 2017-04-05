package requirementsascodeextract.freemarker.words;

import static org.junit.Assert.assertEquals;
import static requirementsascodeextract.freemarker.words.WordProcessor.system;
import static requirementsascodeextract.freemarker.words.WordProcessor.user;

import org.junit.Test;

import requirementsascodeextract.freemarker.systemreaction.GreetUser;
import requirementsascodeextract.freemarker.userevent.EnterName;

public class WordProcessorTest {

  @Test
  public void extractsWordsOfSystemReaction() {
    assertEquals("greet user", system(new GreetUser()));
  }

  @Test
  public void extractsWordsOfUserEvent() {
    assertEquals("enter name", user(EnterName.class));
  }
}
