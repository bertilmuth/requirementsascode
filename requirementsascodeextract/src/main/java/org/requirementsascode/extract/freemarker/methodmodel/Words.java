package org.requirementsascode.extract.freemarker.methodmodel;

import org.apache.commons.lang3.StringUtils;

public class Words {
  public static String getLowerCaseWordsOfClassName(String camelCaseClassName) {
    String[] wordArray = toWordArray(camelCaseClassName);
    String words = wordArrayToLowerCaseString(wordArray);
    return words;
  }

  private static String[] toWordArray(String camelCaseString) {
    String[] wordsArray = StringUtils.splitByCharacterTypeCamelCase(camelCaseString);
    return wordsArray;
  }
  
  private static String wordArrayToLowerCaseString(String[] wordArray) {
    String wordString = StringUtils.join(wordArray, " ").toLowerCase();
    return wordString;
  }
}
