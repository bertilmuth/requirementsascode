package requirementsascodeextract.freemarker.methodmodel;

import org.apache.commons.lang3.StringUtils;

public class WordsOfMethod extends AbstractCamelCaseToWordsMethod {
  public String camelCaseToWords(String camelCaseString) {
    return wordsOf(camelCaseString);
  }
  
  private String wordsOf(String camelCaseString) {
	    String[] wordsArray = StringUtils.splitByCharacterTypeCamelCase(camelCaseString);
	    String words = StringUtils.join(wordsArray, " ").toLowerCase();
	    return words;
	  }
}
