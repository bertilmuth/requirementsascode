package org.requirementsascode.extract.freemarker.methodmodel;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public abstract class AbstractCamelCaseToWordsMethod implements TemplateMethodModelEx {
  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() != 1) {
      throw new TemplateModelException("Wrong number of arguments");
    }
    String camelCaseString = ((SimpleScalar) arguments.get(0)).getAsString();
    String[] wordArray = toWordArray(camelCaseString);
    String words = wordArrayToString(wordArray);
    return new SimpleScalar(words.toLowerCase());
  }

  public abstract String wordArrayToString(String[] wordArray) throws TemplateModelException;

  private String[] toWordArray(String camelCaseString) {
    String[] wordsArray = StringUtils.splitByCharacterTypeCamelCase(camelCaseString);
    return wordsArray;
  }
}
