package requirementsascodeextract.freemarker.methodmodel;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.requirementsascode.Actor;
import org.requirementsascode.UseCaseModelBuilder;

import freemarker.template.TemplateModelException;

public class AsMethodTest {
  @SuppressWarnings("rawtypes")
  @Test
  public void testAsOfMethodForTwoActors() throws TemplateModelException {
    UseCaseModelBuilder builder = UseCaseModelBuilder.newBuilder();
    Actor[] actors = new Actor[] {builder.actor("EndCustomer"), builder.actor("admin")};

    AsOfMethod asOfMethod = new AsOfMethod();
    List arguments = Arrays.asList(new Object[] {actors, " & "});
    assertEquals("end customer & admin", asOfMethod.exec(arguments).toString());
  }
}
