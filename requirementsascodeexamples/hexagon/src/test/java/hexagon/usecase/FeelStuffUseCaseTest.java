package hexagon.usecase;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.TestUseCaseModelRunner;
import org.requirementsascode.UseCaseModel;
import org.requirementsascode.UseCaseModelBuilder;

import hexagon.adapter.stub.RepositoryStub;
import hexagon.adapter.stub.WriterStub;
import hexagon.usecase.HexagonUseCaseModel;
import hexagon.usecaserealization.FeelStuffUseCaseRealization;

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
    testRunner.reactTo(new AsksForPoem(), new AsksForPoem(), new AsksForPoem());
    assertEquals("1;2;3;", testRunner.getRunStepNames());
  }
}
