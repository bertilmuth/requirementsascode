package requirementsascodeextract.freemarker.methodmodel;

import static requirementsascodeextract.freemarker.methodmodel.WordProcessor.wordsOf;

import java.util.List;
import java.util.function.Consumer;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class SystemWordsMethod implements TemplateMethodModelEx {
  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() != 1) {
      throw new TemplateModelException("Wrong number of arguments");
    }
    Consumer<?> systemReaction = (Consumer<?>) arguments.get(0);
    String userString = system(systemReaction);
    return new SimpleScalar(userString);
  }

  private String system(Consumer<?> systemReaction) {
    String camelCaseString = systemReaction.getClass().getSimpleName();
    String words = wordsOf(camelCaseString);
    return words;
  }
}
