package org.requirementsascode.extract.freemarker.methodmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.requirementsascode.extract.freemarker.methodmodel.AfterFirstWordOfMethod;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModelException;

public class AfterFirstWordOfMethodTest {
  @SuppressWarnings("rawtypes")
  @Test
  public void testAfterFirstWordOfMethodForWrongNumberOfArguments() {
    AfterFirstWordOfMethod afterFirstWordOfMethod = new AfterFirstWordOfMethod();
    List arguments = Arrays.asList(new Object[] {});
    try {
      afterFirstWordOfMethod.exec(arguments);
      fail();
    } catch (TemplateModelException expected) {
    }
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testAfterFirstWordOfMethodForOnePart() throws TemplateModelException {
    AfterFirstWordOfMethod afterFirstWordOfMethod = new AfterFirstWordOfMethod();
    List arguments = Arrays.asList(new Object[] {new SimpleScalar("Fly")});
    assertEquals("", afterFirstWordOfMethod.exec(arguments).toString());
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testAfterFirstWordOfMethodForTwoParts() throws TemplateModelException {
    AfterFirstWordOfMethod afterFirstWordOfMethod = new AfterFirstWordOfMethod();
    List arguments = Arrays.asList(new Object[] {new SimpleScalar("EnterName")});
    assertEquals("name", afterFirstWordOfMethod.exec(arguments).toString());
  }
  
  @SuppressWarnings("rawtypes")
  @Test
  public void testAfterFirstWordOfMethodForThreeParts() throws TemplateModelException {
    AfterFirstWordOfMethod afterFirstWordOfMethod = new AfterFirstWordOfMethod();
    List arguments = Arrays.asList(new Object[] {new SimpleScalar("GoGetDrunk")});
    assertEquals("get drunk", afterFirstWordOfMethod.exec(arguments).toString());
  }
}
