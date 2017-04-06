package requirementsascodeextract.freemarker.methodmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModelException;

public class WordsMethodTest {
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
  public void testWordsOfMethod() throws TemplateModelException {
    WordsOfMethod wordsOfMethod = new WordsOfMethod();
    List arguments = Arrays.asList(new Object[] {new SimpleScalar("EnterName")});
    assertEquals("enter name", wordsOfMethod.exec(arguments).toString());
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testFirstWordOfMethodForWrongNumberOfArguments() {
    FirstWordOfMethod firstWordOfMethod = new FirstWordOfMethod();
    List arguments = Arrays.asList(new Object[] {});
    try {
      firstWordOfMethod.exec(arguments);
      fail();
    } catch (TemplateModelException expected) {
    }
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testFirstWordOfMethod() throws TemplateModelException {
    FirstWordOfMethod firstWordOfMethod = new FirstWordOfMethod();
    List arguments = Arrays.asList(new Object[] {new SimpleScalar("EnterName")});
    assertEquals("enter", firstWordOfMethod.exec(arguments).toString());
  }
}
