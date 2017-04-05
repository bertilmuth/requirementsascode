package requirementsascodeextract.freemarker.words;

import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

public class WordProcessor {
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
