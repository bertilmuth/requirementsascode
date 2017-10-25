package org.requirementsascode.extract.freemarker.methodmodel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.requirementsascode.extract.freemarker.userevent.DecidesToQuit;
import org.requirementsascode.extract.freemarker.userevent.EntersName;

import freemarker.template.TemplateModelException;

public class WordsTest {
  @Test
  public void returnsNoWord() throws TemplateModelException {
    assertEquals("", Words.getLowerCaseWordsOfClassName(""));
  }
  
  @Test
  public void returnsTwoLowerCaseWords() throws TemplateModelException {
    assertEquals("enters name", Words.getLowerCaseWordsOfClassName(EntersName.class.getSimpleName()));
  }

  @Test
  public void returnsThreeLowerCaseWords() throws TemplateModelException {
    assertEquals("decides to quit", Words.getLowerCaseWordsOfClassName(DecidesToQuit.class.getSimpleName()));
  }
}
