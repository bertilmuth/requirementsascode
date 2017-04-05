package requirementsascodeextract.freemarker.methodmodel;

import static requirementsascodeextract.freemarker.methodmodel.StepProcessor.wordsOf;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.requirementsascode.Actor;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class AsWordsMethod implements TemplateMethodModelEx {
  @SuppressWarnings("rawtypes")
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() != 2) {
      throw new TemplateModelException("Wrong number of arguments");
    }
    Actor[] actors = (Actor[]) arguments.get(0);
    String separator = (String) arguments.get(1);
    String actorsString = as(actors, separator);
    return new SimpleScalar(actorsString);
  }

  public static String as(Actor[] actors, String separator) {
    List<String> actorNames = new ArrayList<>();
    for (Actor actor : actors) {
      String actorName = wordsOf(actor.getName());
      actorNames.add(actorName);
    }
    String actorsString = StringUtils.join(actorNames, separator).toLowerCase();
    return actorsString;
  }
}
