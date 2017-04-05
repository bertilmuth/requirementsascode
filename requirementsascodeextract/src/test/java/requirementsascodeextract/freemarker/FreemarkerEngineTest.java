package requirementsascodeextract.freemarker;

import java.io.File;
import java.io.OutputStreamWriter;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;
import org.requirementsascode.UseCaseModelRunner;

import requirementsascodeextract.freemarker.systemreaction.GreetUser;

public class FreemarkerEngineTest {
  private FreeMarkerEngine engine;

  @Before
  public void setUp() throws Exception {
    engine = new FreeMarkerEngine();
  }

  @Test
  public void printsUseCaseModelToConsole() throws Exception {
    UseCaseModel useCaseModel =
        UseCaseModelBuilder.newBuilder()
            .useCase("Get greeted")
            .basicFlow()
            .step("S1")
            .system(greetUser())
            .build();

    engine.put("useCaseModel", useCaseModel);
    File templateFile = new File("src/test/resources/example.ftlh");
    engine.process(templateFile, new OutputStreamWriter(System.out));
  }

  private Consumer<UseCaseModelRunner> greetUser() {
    return new GreetUser();
  }
}
