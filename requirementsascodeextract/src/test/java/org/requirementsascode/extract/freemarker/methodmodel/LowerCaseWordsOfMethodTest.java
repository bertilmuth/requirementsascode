package org.requirementsascode.extract.freemarker.methodmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.requirementsascode.extract.freemarker.methodmodel.LowerCaseWordsOfMethod;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModelException;

public class LowerCaseWordsOfMethodTest {
  @SuppressWarnings("rawtypes")
  @Test
  public void testWordsOfMethodForWrongNumberOfArguments() {
    LowerCaseWordsOfMethod wordsOfMethod = new LowerCaseWordsOfMethod();
    List arguments = Arrays.asList(new Object[] {});
    try {
      wordsOfMethod.exec(arguments);
      fail();
    } catch (TemplateModelException expected) {
    }
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testLowerCaseWordsOfMethodForOnePart() throws TemplateModelException {
    LowerCaseWordsOfMethod wordsOfMethod = new LowerCaseWordsOfMethod();
    List arguments = Arrays.asList(new Object[] {new SimpleScalar("fly")});
    assertEquals("fly", wordsOfMethod.exec(arguments).toString());
  }

  @SuppressWarnings("rawtypes")
  @Test
  public void testLowerCaseWordsOfMethodForTwoParts() throws TemplateModelException {
    LowerCaseWordsOfMethod wordsOfMethod = new LowerCaseWordsOfMethod();
    List arguments = Arrays.asList(new Object[] {new SimpleScalar("EnterName")});
    assertEquals("enter name", wordsOfMethod.exec(arguments).toString());
  }
}
