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
  private ConsolePort consolePort;

  @Before
  public void setUp() throws Exception {
    consolePort = new ConsoleStub();
    useCaseModel = 
      new HexagonUseCaseModel(consolePort).buildWith(UseCaseModelBuilder.newBuilder());
    testRunner = new TestUseCaseModelRunner();
  }

  @Test
  public void testBasicFlow() {
    testRunner.run(useCaseModel);
    testRunner.reactTo(new AskForPoem(), new AskForPoem(), new AskForPoem());
    assertEquals("1;2;3;", testRunner.getRunStepNames());
  }
}
