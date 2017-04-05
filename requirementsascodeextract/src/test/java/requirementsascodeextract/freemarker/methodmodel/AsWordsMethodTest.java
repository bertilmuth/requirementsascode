package requirementsascodeextract.freemarker.methodmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseModelBuilder;

import freemarker.template.TemplateModelException;

public class AsWordsMethodTest {
  @SuppressWarnings("rawtypes")
  @Test
  public void testAsWordsMethodForWrongNumberOfArguments() {
    AsWordsMethod asWordsMethod = new AsWordsMethod();
    List arguments = Arrays.asList(new Object[] {});
    try {
      asWordsMethod.exec(arguments);
      fail();
    } catch (TemplateModelException expected) {
    }
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testAsWordsMethod() throws TemplateModelException {
    UseCaseModelBuilder builder = UseCaseModelBuilder.newBuilder();
    Actor[] actors = new Actor[] {builder.actor("EndCustomer"), builder.actor("admin")};

    AsWordsMethod asWordsMethod = new AsWordsMethod();
    List arguments = Arrays.asList(new Object[] {actors, " & "});
    assertEquals("end customer & admin", asWordsMethod.exec(arguments).toString());
  }
}
