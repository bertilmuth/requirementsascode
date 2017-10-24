package org.requirementsascode.extract.freemarker.methodmodel;

import org.apache.commons.lang3.StringUtils;

public class LowerCaseWordsOfMethod extends AbstractCamelCaseToWordsMethod {
  public String wordArrayToString(String[] wordArray) {
    String wordString = StringUtils.join(wordArray, " ").toLowerCase();
    return wordString;
  }
}
