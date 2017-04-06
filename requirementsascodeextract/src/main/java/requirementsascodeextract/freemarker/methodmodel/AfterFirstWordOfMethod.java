package requirementsascodeextract.freemarker.methodmodel;

import org.apache.commons.lang3.StringUtils;

import freemarker.template.TemplateModelException;

public class AfterFirstWordOfMethod extends AbstractCamelCaseToWordsMethod {
  public String wordArrayToString(String[] wordArray) throws TemplateModelException{
    if (wordArray.length == 0) {
      throw new TemplateModelException("Empty array does not have 'first word'");
    }
    String wordString = StringUtils.join(wordArray, " ", 1, wordArray.length);
    return wordString;
  }
}
