package requirementsascodeextract.freemarker.methodmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import freemarker.template.TemplateModelException;
import requirementsascodeextract.freemarker.userevent.EnterName;

public class UserWordsMethodTest {
  @SuppressWarnings("rawtypes")
  @Test
  public void testUserWordsMethodForWrongNumberOfArguments() {
    UserWordsMethod asWordsMethod = new UserWordsMethod();
    List arguments = Arrays.asList(new Object[] {});
    try {
      asWordsMethod.exec(arguments);
      fail();
    } catch (TemplateModelException expected) {
    }
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testUserWordsMethodForTwoActors() throws TemplateModelException {
    UserWordsMethod userWordsMethod = new UserWordsMethod();
    List arguments = Arrays.asList(new Object[] {EnterName.class});
    assertEquals("enter name", userWordsMethod.exec(arguments).toString());
  }
}
