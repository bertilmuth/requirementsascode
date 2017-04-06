package requirementsascodeextract.freemarker.methodmodel;

import org.apache.commons.lang3.StringUtils;

public class WordsOfMethod extends AbstractCamelCaseToWordsMethod {
  public String wordArrayToString(String[] wordArray) {
    String wordString = StringUtils.join(wordArray, " ");
    return wordString;
  }
}
