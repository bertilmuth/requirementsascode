package requirementsascodeextract.freemarker.methodmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import freemarker.template.TemplateModelException;
import requirementsascodeextract.freemarker.systemreaction.GreetUser;

public class SystemWordsMethodTest {
  @SuppressWarnings("rawtypes")
  @Test
  public void testSystemWordsMethodForWrongNumberOfArguments() {
    SystemWordsMethod systemWordsMethod = new SystemWordsMethod();
    List arguments = Arrays.asList(new Object[] {});
    try {
      systemWordsMethod.exec(arguments);
      fail();
    } catch (TemplateModelException expected) {
    }
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testSystemWordsMethod() throws TemplateModelException {
    SystemWordsMethod systemWordsMethod = new SystemWordsMethod();
    List arguments = Arrays.asList(new Object[] {new GreetUser()});
    assertEquals("greet user", systemWordsMethod.exec(arguments).toString());
  }
}
