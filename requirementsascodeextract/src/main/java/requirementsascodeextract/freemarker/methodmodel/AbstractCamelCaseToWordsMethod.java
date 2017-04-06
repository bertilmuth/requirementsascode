package requirementsascodeextract.freemarker.methodmodel;

import java.util.List;

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
    String camelCaseString = (String) ((SimpleScalar) arguments.get(0)).getAsString();
    String words = camelCaseToWords(camelCaseString);
    return new SimpleScalar(words);
  }

  public abstract String camelCaseToWords(String camelCaseString);
}
