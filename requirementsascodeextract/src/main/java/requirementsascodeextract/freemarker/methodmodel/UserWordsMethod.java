package requirementsascodeextract.freemarker.methodmodel;

import static requirementsascodeextract.freemarker.methodmodel.StepProcessor.wordsOf;

import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class UserWordsMethod implements TemplateMethodModelEx {
  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() != 1) {
      throw new TemplateModelException("Wrong number of arguments");
    }
    Class<?> userEvent = (Class<?>) arguments.get(0);
    String userString = user(userEvent);
    return new SimpleScalar(userString);
  }

  private String user(Class<?> eventClass) {
    String camelCaseString = eventClass.getSimpleName();
    String words = wordsOf(camelCaseString);
    return words;
  }
}
