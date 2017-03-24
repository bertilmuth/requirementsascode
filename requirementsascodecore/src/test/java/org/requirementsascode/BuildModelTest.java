package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.requirementsascode.testutil.Names.ALTERNATIVE_FLOW;
import static org.requirementsascode.testutil.Names.USE_CASE_2;
import static org.requirementsascode.testutil.Names.BASIC_FLOW;
import static org.requirementsascode.testutil.Names.CUSTOMER;
import static org.requirementsascode.testutil.Names.CUSTOMER_ENTERS_TEXT;
import static org.requirementsascode.testutil.Names.CUSTOMER_ENTERS_TEXT_AGAIN;
import static org.requirementsascode.testutil.Names.SYSTEM_DISPLAYS_NUMBER;
import static org.requirementsascode.testutil.Names.SYSTEM_DISPLAYS_TEXT;
import static org.requirementsascode.testutil.Names.SYSTEM_DISPLAYS_TEXT_AGAIN;
import static org.requirementsascode.testutil.Names.USE_CASE;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.requirementsascode.testutil.EnterNumber;
import org.requirementsascode.testutil.EnterText;

public class BuildModelTest extends AbstractTestCase{			
	@Before
	public void setup() {
		setupWith(new TestUseCaseRunner());
	}
	
	@Test
	public void createsNoUseCase() {
		UseCaseModelBuilder useCaseModelBuilder = new UseCaseModelBuilder(useCaseRunner);
		UseCaseModel useCaseModel = useCaseModelBuilder.build();
		
		Collection<UseCase> useCases = useCaseModel.useCases();
		assertEquals(0, useCases.size());
	}

	@Test
	public void createsSingleUseCase() {
		UseCaseModelBuilder useCaseModelBuilder = new UseCaseModelBuilder(useCaseRunner);
		useCaseModelBuilder.useCase(USE_CASE);
		UseCaseModel useCaseModel = useCaseModelBuilder.build();
		
		assertTrue(useCaseModel.hasUseCase(USE_CASE));

		Collection<UseCase> useCases = useCaseModel.useCases();
		assertEquals(1, useCases.size());
		assertEquals(USE_CASE, useCases.iterator().next().name());
	}

	@Test
	public void createsTwoUseCases() {
		UseCaseModelBuilder useCaseModelBuilder = new UseCaseModelBuilder(useCaseRunner);
		useCaseModelBuilder.useCase(USE_CASE);
		useCaseModelBuilder.useCase(USE_CASE_2);
		UseCaseModel useCaseModel = useCaseModelBuilder.build();

		Collection<UseCase> useCases = useCaseModel.useCases();
		assertEquals(2, useCases.size());
	}
	
	@Test
	public void implicitlyCreatesBasicFlow() {
		UseCaseModelBuilder useCaseModelBuilder = new UseCaseModelBuilder(useCaseRunner);
		useCaseModelBuilder.useCase(USE_CASE);
		UseCaseModel useCaseModel = useCaseModelBuilder.build();

		UseCase useCase = useCaseModel.findUseCase(USE_CASE).get();
		Collection<UseCaseFlow> flows = useCase.flows();
		
		assertEquals(1, flows.size());
		assertTrue(useCase.hasFlow(BASIC_FLOW));
		assertEquals(BASIC_FLOW, flows.iterator().next().name());
	}

	@Test
	public void createsNoSteps() {
		UseCaseModelBuilder useCaseModelBuilder = new UseCaseModelBuilder(useCaseRunner);
		useCaseModelBuilder.useCase(USE_CASE);
		UseCaseModel useCaseModel = useCaseModelBuilder.build();

		UseCase useCase = useCaseModel.findUseCase(USE_CASE).get();
		Collection<UseCaseStep> steps = useCase.steps();
		assertEquals(0, steps.size());
	}
	
	@Test
	public void createsSingleStepThatHandlesUserEvent() {
		UseCaseModelBuilder useCaseModelBuilder = new UseCaseModelBuilder(useCaseRunner);
		UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);
		
		UseCaseModel useCaseModel =
			useCasePart
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).user(EnterText.class).system(displayEnteredText())
			.build();
		
		UseCase useCase = useCasePart.useCase();
		Collection<UseCaseStep> steps = useCase.steps();
		assertEquals(1, steps.size());
		
		UseCaseStep step = steps.iterator().next();
		assertEquals(CUSTOMER_ENTERS_TEXT, step.name());
		assertEquals(USE_CASE, step.useCase().name());	
		assertEquals(useCaseModel.userActor(), step.as().actors()[0]);
	}
	
	@Test
	public void createsSingleStepThatHandlesSystemEvent() {		
		UseCaseModelBuilder useCaseModelBuilder = new UseCaseModelBuilder(useCaseRunner);
		UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);
		
		UseCaseModel useCaseModel = 
			useCasePart
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).handle(EnterText.class).system(displayEnteredText())
			.build();
		
		UseCase useCase = useCaseModel.findUseCase(USE_CASE).get();
		Collection<UseCaseStep> steps = useCase.steps();
		assertEquals(1, steps.size());
		
		UseCaseStep step = steps.iterator().next();
		assertEquals(SYSTEM_DISPLAYS_TEXT, step.name());
		assertEquals(USE_CASE, step.useCase().name());	
		assertEquals(useCaseModel.systemActor(), step.as().actors()[0]);
	}
	
	@Test
	public void createsSingleStepThatPerformsSystemReactionAutomatically() {	
		UseCaseModelBuilder useCaseModelBuilder = new UseCaseModelBuilder(useCaseRunner);
		UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);
		
		UseCaseModel useCaseModel = 
			useCasePart
				.basicFlow()
					.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
			.build();
						
		Collection<UseCaseStep> steps = useCasePart.useCase().steps();
		assertEquals(1, steps.size());
		
		UseCaseStep step = steps.iterator().next();
		assertEquals(SYSTEM_DISPLAYS_TEXT, step.name());
		assertEquals(USE_CASE, step.useCase().name());	
		assertEquals(useCaseModel.systemActor(), step.as().actors()[0]);
	}
	
	@Test
	public void createsSingleStepThatPerformsSystemReactionAutomaticallyForSpecificActor() {
		UseCaseModelBuilder useCaseModelBuilder = new UseCaseModelBuilder(useCaseRunner);
		Actor customer = useCaseModelBuilder.actor(CUSTOMER);
		UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

		UseCaseModel useCaseModel = 
			useCasePart
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).as(customer).system(displayConstantText())
			.build();
				
		Collection<UseCaseStep> steps = useCasePart.useCase().steps();
		assertEquals(1, steps.size());
		
		UseCaseStep step = steps.iterator().next();
		assertEquals(CUSTOMER_ENTERS_TEXT, step.name());
		assertEquals(USE_CASE, step.useCase().name());
		assertTrue(useCaseModel.hasActor(customer.name()));
		assertEquals(customer, step.as().actors()[0]);
	}
	
	@Test
	public void createsSingleActorWithSingleUseCase() {	
		UseCaseModelBuilder useCaseModelBuilder = new UseCaseModelBuilder(useCaseRunner);
		Actor customer = useCaseModelBuilder.actor(CUSTOMER);
		UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);
		
		UseCaseModel useCaseModel = 
			useCasePart
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).as(customer).user(EnterText.class).system(displayEnteredText())
			.build();
		
		Actor customerActor = useCaseModel.findActor(CUSTOMER).get();
		Set<UseCase> useCases = customerActor.useCases();
		assertEquals(1, useCases.size());
		
		UseCase actualUseCase = useCases.iterator().next();
		assertEquals(USE_CASE, actualUseCase.name());
	}
	
	@Test
	public void createsSingleActorWithSingleUseCaseStep() {
		UseCaseModelBuilder useCaseModelBuilder = new UseCaseModelBuilder(useCaseRunner);
		Actor customer = useCaseModelBuilder.actor(CUSTOMER);
		UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);
		
		UseCaseModel useCaseModel = 
			useCasePart
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).as(customer).user(EnterText.class).system(displayEnteredText())
			.build();

		Actor customerActor = useCaseModel.findActor(customer.name()).get();
		List<UseCaseStep> steps = customerActor.stepsOf(useCasePart.useCase());
		UseCaseStep step = steps.get(0);
		
		assertEquals(CUSTOMER_ENTERS_TEXT, step.name());
		assertEquals(CUSTOMER_ENTERS_TEXT, step.toString());
		assertEquals(USE_CASE, step.useCase().name());		
		assertEquals(customer, step.as().actors()[0]);
	}
	
	@Test
	@Ignore
	public void createsTwoActorsWithSingleUseCaseStep() {
		UseCaseModelBuilder useCaseModelBuilder = new UseCaseModelBuilder(useCaseRunner);
		Actor customer = useCaseModelBuilder.actor(CUSTOMER);
		Actor anotherActor = useCaseModelBuilder.actor("Another Actor");
		UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);
				
		UseCaseModel useCaseModel = 
			useCasePart
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT).as(customer, anotherActor).user(EnterText.class).system(displayEnteredText())
			.build();

		Actor customerActor = useCaseModel.findActor(customer.name()).get();		
		List<UseCaseStep> steps = customerActor.stepsOf(useCasePart.useCase());
		UseCaseStep step = steps.get(0);
		
		assertEquals(CUSTOMER_ENTERS_TEXT, step.name());
		assertEquals(CUSTOMER_ENTERS_TEXT, step.toString());
		assertEquals(USE_CASE, step.useCase().name());		
		assertEquals(customer, step.as().actors()[0]);
		
		steps = anotherActor.stepsOf(useCasePart.useCase());
		step = steps.get(0);
		
		assertEquals(CUSTOMER_ENTERS_TEXT, step.name());
		assertEquals(anotherActor, step.as().actors()[1]);
	}
	
	@Test
	@Ignore
	public void createsSingleStepWithNoPreviousStep() {
		UseCase useCase = useCaseModel.useCase(USE_CASE);
		useCase
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText());

		Collection<UseCaseStep> steps = useCase.steps();
		assertEquals(1, steps.size());
		
		Optional<UseCaseStep> previousStep = steps.iterator().next().previousStepInFlow();
		
		assertFalse(previousStep.isPresent());
	}

	@Test
	@Ignore
	public void createsTwoSteps() {
		UseCase namedUseCase = useCaseModel.useCase(USE_CASE);
		
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
		assertEquals(USE_CASE, step.useCase().name());

		step = stepIt.next();
		assertEquals(SYSTEM_DISPLAYS_NUMBER, step.name());
		assertEquals(USE_CASE, step.useCase().name());
	}
	
	@Test
	@Ignore
	public void createsTwoStepsAndCheckIfTheyExistInUseCaseByName() {
		UseCase namedUseCase = useCaseModel.useCase(USE_CASE);
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
	@Ignore
	public void createsTwoStepsInBasicFlowAndCheckIfTheyExistByIndex() {
		UseCase namedUseCase = useCaseModel.useCase(USE_CASE);
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
	@Ignore
	public void createsOneStepInAlternatvieFlowAndCheckIfItExistsByIndex() {
		UseCase namedUseCase = useCaseModel.useCase(USE_CASE);
		namedUseCase
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
			.flow(ALTERNATIVE_FLOW)
				.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());
			
		List<UseCaseStep> steps = namedUseCase.findFlow(ALTERNATIVE_FLOW).steps();
		assertEquals(1, steps.size());
		
		assertEquals(SYSTEM_DISPLAYS_TEXT_AGAIN, steps.get(0).name());
	}
	
	@Test
	@Ignore
	public void createsTwoStepsAndPreviousStepOfSecondOneIsFirstOne() {
		UseCase useCase = useCaseModel.useCase(USE_CASE);
			
		useCase
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
				.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());

		UseCaseStep firstUseCaseStep = useCase.findStep(SYSTEM_DISPLAYS_TEXT);
		UseCaseStep secondUseCaseStep = useCase.findStep(SYSTEM_DISPLAYS_TEXT_AGAIN);
		
		assertEquals(firstUseCaseStep, secondUseCaseStep.previousStepInFlow().get());
	}
	
	@Test
	@Ignore
	public void createsTwoStepsInDifferentFlowsThatBothHaveNoPreviousSteps() { 
		UseCase useCaseInFirstFlow = useCaseModel.useCase(USE_CASE);
		useCaseInFirstFlow
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())	
			.flow(ALTERNATIVE_FLOW)
				.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());

		UseCaseStep firstUseCaseStep = useCaseInFirstFlow.findStep(SYSTEM_DISPLAYS_TEXT);
		UseCaseStep secondUseCaseStep = useCaseInFirstFlow.findStep(SYSTEM_DISPLAYS_TEXT_AGAIN);
		
		assertFalse(firstUseCaseStep.previousStepInFlow().isPresent());
		assertFalse(secondUseCaseStep.previousStepInFlow().isPresent());
	}
	
	@Test
	@Ignore
	public void useCasesAreUniquelyIdentifiedByName() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow()
				.step(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())		
			.flow(ALTERNATIVE_FLOW)
				.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());		
		
		assertEquals(1, useCaseModel.useCases().size());
		assertEquals(useCaseModel.findUseCase(USE_CASE).get(), useCaseModel.useCases().iterator().next());
	}
	
	@Test
	@Ignore
	public void flowsAreUniquelyIdentifiedByName() {		
		useCaseModel.useCase(USE_CASE).flow(ALTERNATIVE_FLOW);
		
		UseCase useCase = useCaseModel.findUseCase(USE_CASE).get();
		UseCaseFlow existingFlow = useCase.findFlow(ALTERNATIVE_FLOW);
		
		UseCase uc = useCase;
		assertEquals(2, uc.flows().size()); // This is 2 because the basic flow always exists
		
		Iterator<UseCaseFlow> flowIt = uc.flows().iterator();
		assertEquals(useCase.basicFlow(), flowIt.next());
		assertEquals(existingFlow, flowIt.next());
	}
	
	@Test
	@Ignore
	public void thereIsOnlyOneBasicFlowPerUseCase() {		
		useCaseModel.useCase(USE_CASE)
			.basicFlow();
		
		useCaseModel.findUseCase(USE_CASE).get()
			.basicFlow();
		
		UseCase uc = useCaseModel.findUseCase(USE_CASE).get();
		assertEquals(1, uc.flows().size());
	}
	
	@Test
	@Ignore
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
	@Ignore
	public void actorsCanBeReusedBetweenUseCases() {
		useCaseModel
			.useCase(USE_CASE)
				.basicFlow()
					.step(CUSTOMER_ENTERS_TEXT)
						.as(customer).user(EnterText.class).system(displayEnteredText())
						
			.useCase(USE_CASE_2)
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
