package requirementsascodeextract.freemarker.methodmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModelException;

public class WordsOfMethodTest {
  @SuppressWarnings("rawtypes")
  @Test
  public void testWordsOfMethodForWrongNumberOfArguments() {
    WordsOfMethod wordsOfMethod = new WordsOfMethod();
    List arguments = Arrays.asList(new Object[] {});
    try {
      wordsOfMethod.exec(arguments);
      fail();
    } catch (TemplateModelException expected) {
    }
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testWordsOfMethodForOnePart() throws TemplateModelException {
    WordsOfMethod wordsOfMethod = new WordsOfMethod();
    List arguments = Arrays.asList(new Object[] {new SimpleScalar("fly")});
    assertEquals("fly", wordsOfMethod.exec(arguments).toString());
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testWordsOfMethodForTwoParts() throws TemplateModelException {
    WordsOfMethod wordsOfMethod = new WordsOfMethod();
    List arguments = Arrays.asList(new Object[] {new SimpleScalar("EnterName")});
    assertEquals("enter name", wordsOfMethod.exec(arguments).toString());
  }
}
