package requirementsascodeextract.freemarker.methodmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.requirementsascode.Actor;

public class StepProcessor {
  public static String as(Actor[] actors, String separator) {
    List<String> actorNames = new ArrayList<>();
    for (Actor actor : actors) {
      String actorName = wordsOf(actor.getName());
      actorNames.add(actorName);
    }
    String actorsString = StringUtils.join(actorNames, separator).toLowerCase();
    return actorsString;
  }

  public static String user(Class<?> eventClass) {
    String camelCaseString = eventClass.getSimpleName();
    String words = wordsOf(camelCaseString);
    return words;
  }

  public static String system(Consumer<?> systemReaction) {
    String camelCaseString = systemReaction.getClass().getSimpleName();
    String words = wordsOf(camelCaseString);
    return words;
  }

  public static String wordsOf(String camelCaseString) {
    String[] wordsArray = StringUtils.splitByCharacterTypeCamelCase(camelCaseString);
    String words = StringUtils.join(wordsArray, " ").toLowerCase();
    return words;
  }
}
