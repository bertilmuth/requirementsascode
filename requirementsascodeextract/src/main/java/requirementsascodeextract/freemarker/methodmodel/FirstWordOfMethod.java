package requirementsascodeextract.freemarker.methodmodel;

import freemarker.template.TemplateModelException;

public class FirstWordOfMethod extends AbstractCamelCaseToWordsMethod {
  public String wordArrayToString(String[] wordArray) throws TemplateModelException{
    if (wordArray.length == 0) {
      throw new TemplateModelException("Empty array does not have 'first word'");
    }
    String wordString = wordArray[0].toLowerCase();
    return wordString;
  }
}
