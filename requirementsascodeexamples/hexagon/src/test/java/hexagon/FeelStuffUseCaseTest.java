package hexagon;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.TestUseCaseModelRunner;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;

public class FeelStuffUseCaseTest {

  private UseCaseModel useCaseModel;
  private TestUseCaseModelRunner testRunner;
  private FeelStuffUseCaseRealization feelStuffUseCaseRealization;

  @Before
  public void setUp() throws Exception {
    feelStuffUseCaseRealization =
        new FeelStuffUseCaseRealization(new WriterStub(), new RepositoryStub());
    useCaseModel =
        new HexagonUseCaseModel(feelStuffUseCaseRealization)
            .buildWith(UseCaseModelBuilder.newBuilder());
    testRunner = new TestUseCaseModelRunner();
  }

  @Test
  public void testBasicFlow() {
    testRunner.run(useCaseModel);
    testRunner.reactTo(new AskForPoem(), new AskForPoem(), new AskForPoem());
    assertEquals("1;2;3;", testRunner.getRunStepNames());
  }
}
