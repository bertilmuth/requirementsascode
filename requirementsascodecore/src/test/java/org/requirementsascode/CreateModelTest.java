package org.requirementsascode;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.event.EnterNumber;
import org.requirementsascode.event.EnterText;

public class CreateModelTest extends AbstractTestCase{	
	private static final String USE_CASE = "Use Case";
	private static final String SAY_HELLO_USE_CASE = "Say Hello Use Case";
	private static final String SAY_WORLD_USE_CASE = "Say World Use Case";
	
	private static final String BASIC_FLOW = "Basic Flow";
	private static final String ANOTHER_FLOW = "Another Flow";

	private static final String CUSTOMER_ENTERS_TEXT = "Customer enters text";
	private static final String CUSTOMER_ENTERS_TEXT_AGAIN = "Customer enters text again";
	
	private static final String SYSTEM_DISPLAYS_TEXT = "System displays text";
	private static final String SYSTEM_DISPLAYS_TEXT_AGAIN = "System displays text again";
	private static final String SYSTEM_DISPLAYS_NUMBER = "System displays number";

	@Before
	public void setup() {
		setupWith(new TestUseCaseRunner());
	}
	
	@Test
	public void createsNoUseCase() {
		Collection<UseCase> useCases = useCaseModel.useCases();
		assertEquals(0, useCases.size());
	}

	@Test
	public void createsSingleUseCase() {
		useCaseModel.useCase(SAY_HELLO_USE_CASE);
		
		assertTrue(useCaseModel.hasUseCase(SAY_HELLO_USE_CASE));

		Collection<UseCase> useCases = useCaseModel.useCases();
		assertEquals(1, useCases.size());
		assertEquals(SAY_HELLO_USE_CASE, useCases.iterator().next().name());
	}

	@Test
	public void createsTwoUseCases() {
		useCaseModel.useCase(SAY_HELLO_USE_CASE);
		useCaseModel.useCase(SAY_WORLD_USE_CASE);

		Collection<UseCase> useCases = useCaseModel.useCases();
		assertEquals(2, useCases.size());
	}
	
	@Test
	public void implicitlyCreatesBasicFlow() {
		UseCase useCase = useCaseModel.useCase(SAY_HELLO_USE_CASE);
		Collection<UseCaseFlow> flows = useCase.flows();
		
		assertEquals(1, flows.size());
		assertTrue(useCase.hasFlow(BASIC_FLOW));
		assertEquals(BASIC_FLOW, flows.iterator().next().name());
	}

	@Test
	public void createsNoSteps() {
		UseCase useCase = useCaseModel.useCase(SAY_HELLO_USE_CASE);

		Collection<UseCaseStep> steps = useCase.steps();
		assertEquals(0, steps.size());
	}
	
	@Test
	public void createsSingleStepThatHandlesUserEvent() {		
		UseCase useCase = useCaseModel.useCase(SAY_HELLO_USE_CASE);
		useCase
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText());
				
		Collection<UseCaseStep> steps = useCase.steps();
		assertEquals(1, steps.size());
		
		UseCaseStep step = steps.iterator().next();
		assertEquals(CUSTOMER_ENTERS_TEXT, step.name());
		assertEquals(SAY_HELLO_USE_CASE, step.useCase().name());	
		assertEquals(useCaseModel.userActor(), step.as().actors()[0]);
	}
	
	@Test
	public void createsSingleStepThatHandlesSystemEvent() {		
		UseCase useCase = useCaseModel.useCase(SAY_HELLO_USE_CASE);
		useCase
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).handle(EnterText.class).system(displayEnteredText());
				
		Collection<UseCaseStep> steps = useCase.steps();
		assertEquals(1, steps.size());
		
		UseCaseStep step = steps.iterator().next();
		assertEquals(SYSTEM_DISPLAYS_TEXT, step.name());
		assertEquals(SAY_HELLO_USE_CASE, step.useCase().name());	
		assertEquals(useCaseModel.systemActor(), step.as().actors()[0]);
	}
	
	@Test
	public void createsSingleStepThatPerformsSystemReactionAutomatically() {		
		UseCase useCase = useCaseModel.useCase(SAY_HELLO_USE_CASE);
		useCase
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText());
				
		Collection<UseCaseStep> steps = useCase.steps();
		assertEquals(1, steps.size());
		
		UseCaseStep step = steps.iterator().next();
		assertEquals(SYSTEM_DISPLAYS_TEXT, step.name());
		assertEquals(SAY_HELLO_USE_CASE, step.useCase().name());	
		assertEquals(useCaseModel.systemActor(), step.as().actors()[0]);
	}
	
	@Test
	public void createsSingleStepThatPerformsSystemReactionAutomaticallyForSpecificActor() {
		
		UseCase useCase = useCaseModel.useCase(SAY_HELLO_USE_CASE);
		useCase
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT).as(customer).system(displayConstantText());
				
		Collection<UseCaseStep> steps = useCase.steps();
		assertEquals(1, steps.size());
		
		UseCaseStep step = steps.iterator().next();
		assertEquals(CUSTOMER_ENTERS_TEXT, step.name());
		assertEquals(SAY_HELLO_USE_CASE, step.useCase().name());
		assertTrue(useCaseModel.hasActor(customer.name()));
		assertEquals(customer, step.as().actors()[0]);
	}
	
	@Test
	public void createsSingleActorWithSingleUseCase() {		
		UseCase useCase = useCaseModel.useCase(SAY_HELLO_USE_CASE);
		useCase
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.as(customer).user(EnterText.class).system(displayEnteredText());

		Actor customerActor = useCaseModel.findActor(customer.name()).get();
		Set<UseCase> useCases = customerActor.useCases();
		assertEquals(1, useCases.size());
		
		UseCase actualUseCase = useCases.iterator().next();
		assertEquals(SAY_HELLO_USE_CASE, actualUseCase.name());
	}
	
	@Test
	public void createsSingleActorWithSingleUseCaseStep() {		
		UseCase useCase = useCaseModel.useCase(SAY_HELLO_USE_CASE);
		useCase
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.as(customer).user(EnterText.class).system(displayEnteredText());

		Actor customerActor = useCaseModel.findActor(customer.name()).get();
		List<UseCaseStep> steps = customerActor.steps(useCase);
		UseCaseStep step = steps.get(0);
		
		assertEquals(CUSTOMER_ENTERS_TEXT, step.name());
		assertEquals(CUSTOMER_ENTERS_TEXT, step.toString());
		assertEquals(SAY_HELLO_USE_CASE, step.useCase().name());		
		assertEquals(customer, step.as().actors()[0]);
	}
	
	@Test
	public void createsTwoActorsWithSingleUseCaseStep() {		
		Actor anotherActor = useCaseModel.actor("Another Actor");
		UseCase useCase = useCaseModel.useCase(SAY_HELLO_USE_CASE);
		
		useCase
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.as(customer, anotherActor).user(EnterText.class).system(displayEnteredText());

		Actor customerActor = useCaseModel.findActor(customer.name()).get();		
		List<UseCaseStep> steps = customerActor.steps(useCase);
		UseCaseStep step = steps.get(0);
		
		assertEquals(CUSTOMER_ENTERS_TEXT, step.name());
		assertEquals(CUSTOMER_ENTERS_TEXT, step.toString());
		assertEquals(SAY_HELLO_USE_CASE, step.useCase().name());		
		assertEquals(customer, step.as().actors()[0]);
		
		steps = anotherActor.steps(useCase);
		step = steps.get(0);
		
		assertEquals(CUSTOMER_ENTERS_TEXT, step.name());
		assertEquals(anotherActor, step.as().actors()[1]);
	}
	
	@Test
	public void createsSingleStepWithNoPreviousStep() {
		UseCase useCase = useCaseModel.useCase(SAY_HELLO_USE_CASE);
		useCase
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText());

		Collection<UseCaseStep> steps = useCase.steps();
		assertEquals(1, steps.size());
		
		Optional<UseCaseStep> previousStep = steps.iterator().next().previousStepInFlow();
		
		assertFalse(previousStep.isPresent());
	}

	@Test
	public void createsTwoSteps() {
		UseCase namedUseCase = useCaseModel.useCase(SAY_HELLO_USE_CASE);
		
		namedUseCase
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText()) 
				.step(SYSTEM_DISPLAYS_NUMBER)
					.as(customer).user(EnterNumber.class).system(displayEnteredNumber());

		assertTrue(useCaseModel.actors().contains(useCaseModel.systemActor()));
		assertTrue(useCaseModel.actors().contains(customer));
		
		Collection<UseCaseStep> steps = namedUseCase.steps();
		assertEquals(2, steps.size());

		Iterator<UseCaseStep> stepIt = steps.iterator();
		UseCaseStep step = stepIt.next();
		assertEquals(SYSTEM_DISPLAYS_TEXT, step.name());
		assertEquals(SAY_HELLO_USE_CASE, step.useCase().name());

		step = stepIt.next();
		assertEquals(SYSTEM_DISPLAYS_NUMBER, step.name());
		assertEquals(SAY_HELLO_USE_CASE, step.useCase().name());
	}
	
	@Test
	public void createsTwoStepsAndCheckIfTheyExistInUseCaseByName() {
		UseCase namedUseCase = useCaseModel.useCase(SAY_HELLO_USE_CASE);
		namedUseCase
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
				.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());
		
		Collection<UseCaseStep> steps = namedUseCase.steps();
		assertEquals(2, steps.size());

		boolean firstUseCaseStepExists = namedUseCase.hasStep(SYSTEM_DISPLAYS_TEXT);
		boolean secondUseCaseStepExists = namedUseCase.hasStep(SYSTEM_DISPLAYS_TEXT_AGAIN);
		
		assertTrue(firstUseCaseStepExists);
		assertTrue(secondUseCaseStepExists);
	}
	
	@Test
	public void createsTwoStepsInBasicFlowAndCheckIfTheyExistByIndex() {
		UseCase namedUseCase = useCaseModel.useCase(SAY_HELLO_USE_CASE);
		namedUseCase
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
				.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());
		
		List<UseCaseStep> steps = namedUseCase.basicFlow().steps();
		assertEquals(2, steps.size());
		
		assertEquals(SYSTEM_DISPLAYS_TEXT, steps.get(0).name());
		assertEquals(SYSTEM_DISPLAYS_TEXT_AGAIN, steps.get(1).name());
	}
	
	@Test
	public void createsOneStepInAlternatvieFlowAndCheckIfItExistsByIndex() {
		UseCase namedUseCase = useCaseModel.useCase(SAY_HELLO_USE_CASE);
		namedUseCase
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
			.flow(ANOTHER_FLOW)
				.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());
			
		List<UseCaseStep> steps = namedUseCase.findFlow(ANOTHER_FLOW).get().steps();
		assertEquals(1, steps.size());
		
		assertEquals(SYSTEM_DISPLAYS_TEXT_AGAIN, steps.get(0).name());
	}
	
	@Test
	public void createsTwoStepsAndPreviousStepOfSecondOneIsFirstOne() {
		UseCase useCase = useCaseModel.useCase(SAY_HELLO_USE_CASE);
			
		useCase
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
				.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());

		UseCaseStep firstUseCaseStep = useCase.findStep(SYSTEM_DISPLAYS_TEXT).get();
		UseCaseStep secondUseCaseStep = useCase.findStep(SYSTEM_DISPLAYS_TEXT_AGAIN).get();
		
		assertEquals(firstUseCaseStep, secondUseCaseStep.previousStepInFlow().get());
	}
	
	@Test
	public void createsTwoStepsInDifferentFlowsThatBothHaveNoPreviousSteps() { 
		UseCase useCaseInFirstFlow = useCaseModel.useCase(SAY_HELLO_USE_CASE);
		useCaseInFirstFlow
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())	
			.flow(ANOTHER_FLOW)
				.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());

		UseCaseStep firstUseCaseStep = useCaseInFirstFlow.findStep(SYSTEM_DISPLAYS_TEXT).get();
		UseCaseStep secondUseCaseStep = useCaseInFirstFlow.findStep(SYSTEM_DISPLAYS_TEXT_AGAIN).get();
		
		assertFalse(firstUseCaseStep.previousStepInFlow().isPresent());
		assertFalse(secondUseCaseStep.previousStepInFlow().isPresent());
	}
	
	@Test
	public void useCasesAreUniquelyIdentifiedByName() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())		
			.flow(ANOTHER_FLOW)
				.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());		
		
		assertEquals(1, useCaseModel.useCases().size());
		assertEquals(useCaseModel.findUseCase(USE_CASE).get(), useCaseModel.useCases().iterator().next());
	}
	
	@Test
	public void flowsAreUniquelyIdentifiedByName() {		
		useCaseModel.useCase(USE_CASE).flow(ANOTHER_FLOW);
		
		UseCase useCase = useCaseModel.findUseCase(USE_CASE).get();
		Optional<UseCaseFlow> existingFlow = useCase.findFlow(ANOTHER_FLOW);
		
		UseCase uc = useCase;
		assertEquals(2, uc.flows().size()); // This is 2 because the basic flow always exists
		
		Iterator<UseCaseFlow> flowIt = uc.flows().iterator();
		assertEquals(useCase.basicFlow(), flowIt.next());
		assertEquals(existingFlow.get(), flowIt.next());
	}
	
	@Test
	public void thereIsOnlyOneBasicFlowPerUseCase() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow();
		
		useCaseModel.findUseCase(USE_CASE).get()
			.basicFlow();
		
		UseCase uc = useCaseModel.findUseCase(USE_CASE).get();
		assertEquals(1, uc.flows().size());
	}
	
	@Test
	public void actorsCanBeReusedInUseCase() {
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT)
					.as(customer).user(EnterText.class).system(displayEnteredText())
				.step(CUSTOMER_ENTERS_TEXT_AGAIN)
					.as(customer).user(EnterText.class).system(displayEnteredText());
		
		Collection<UseCaseStep> steps = useCaseModel.steps();
		assertEquals(2, steps.size());
		
		Iterator<UseCaseStep> stepsIt = steps.iterator();
		Actor actor1 = stepsIt.next().as().actors()[0];
		Actor actor2 = stepsIt.next().as().actors()[0];
		
		assertTrue(actor1 == actor2);
		assertEquals(customer, actor1);
	}
	
	@Test
	public void actorsCanBeReusedBetweenUseCases() {
		useCaseModel
			.useCase("Use Case 1")
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EnterText.class).system(displayEnteredText())
						
			.useCase("Use Case 2")
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT_AGAIN)
						.as(customer).user(EnterText.class).system(displayEnteredText());		
				
		Collection<UseCaseStep> steps = useCaseModel.steps();
		assertEquals(2, steps.size());
		
		Iterator<UseCaseStep> stepsIt = steps.iterator();
		Actor actor1 = stepsIt.next().as().actors()[0];
		Actor actor2 = stepsIt.next().as().actors()[0];
		
		assertTrue(actor1 == actor2);
		assertEquals(customer, actor1);
	}
}
