package org.requirementsascode.extract.freemarker.methodmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.requirementsascode.extract.freemarker.methodmodel.util.Words;
import org.requirementsascode.extract.freemarker.usercommand.DecidesToQuit;
import org.requirementsascode.extract.freemarker.usercommand.EntersName;

import freemarker.template.TemplateModelException;

public class WordsTest {
  @Test
  public void returnsTwoLowerCaseWords() throws TemplateModelException {
    assertEquals("enters name", Words.getLowerCaseWordsOfClassName(EntersName.class));
  }

  @Test
  public void returnsThreeLowerCaseWords() throws TemplateModelException {
    assertEquals("decides to quit", Words.getLowerCaseWordsOfClassName(DecidesToQuit.class));
  }
}
