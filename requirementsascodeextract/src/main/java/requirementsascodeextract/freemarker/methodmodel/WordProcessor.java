package requirementsascodeextract.freemarker.methodmodel;

import org.apache.commons.lang3.StringUtils;

class WordProcessor {
  public static String wordsOf(String camelCaseString) {
    String[] wordsArray = StringUtils.splitByCharacterTypeCamelCase(camelCaseString);
    String words = StringUtils.join(wordsArray, " ").toLowerCase();
    return words;
  }
}
