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
	  WordsOfMethod wordsMethod = new WordsOfMethod();
    List arguments = Arrays.asList(new Object[] {});
    try {
      wordsMethod.exec(arguments);
      fail();
    } catch (TemplateModelException expected) {
    }
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testWordsOfMethod() throws TemplateModelException {
	  WordsOfMethod wordsMethod = new WordsOfMethod();
    List arguments = Arrays.asList(new Object[] {new SimpleScalar("EnterName")});
    assertEquals("enter name", wordsMethod.exec(arguments).toString());
  }
}
