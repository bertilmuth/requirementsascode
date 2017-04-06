package requirementsascodeextract.freemarker.methodmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModelException;

public class FirstWordOfMethodTest {
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
  public void testFirstWordOfMethodForOnePart() throws TemplateModelException {
    FirstWordOfMethod firstWordOfMethod = new FirstWordOfMethod();
    List arguments = Arrays.asList(new Object[] {new SimpleScalar("fly")});
    assertEquals("fly", firstWordOfMethod.exec(arguments).toString());
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testFirstWordOfMethodForTwoParts() throws TemplateModelException {
    FirstWordOfMethod firstWordOfMethod = new FirstWordOfMethod();
    List arguments = Arrays.asList(new Object[] {new SimpleScalar("EnterName")});
    assertEquals("enter", firstWordOfMethod.exec(arguments).toString());
  }
}
