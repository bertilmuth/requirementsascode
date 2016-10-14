package org.requirementsascode;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.requirementsascode.event.EnterNumberEvent;
import org.requirementsascode.event.EnterTextEvent;

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

	@Test
	public void shouldCreateUseCaseModelWithNoUseCase() {
		Collection<UseCase> useCases = useCaseModel.getUseCases();
		assertEquals(0, useCases.size());
	}

	@Test
	public void shouldCreateSingleUseCase() {
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE);
		
		assertTrue(useCaseModel.hasUseCase(SAY_HELLO_USE_CASE));

		Collection<UseCase> useCases = useCaseModel.getUseCases();
		assertEquals(1, useCases.size());
		assertEquals(SAY_HELLO_USE_CASE, useCases.iterator().next().getName());
	}

	@Test
	public void shouldCreateTwoUseCases() {
		useCaseModel.newUseCase(SAY_HELLO_USE_CASE);
		useCaseModel.newUseCase(SAY_WORLD_USE_CASE);

		Collection<UseCase> useCases = useCaseModel.getUseCases();
		assertEquals(2, useCases.size());
	}
	
	@Test
	public void shouldHaveBasicFlowInAnyCase() {
		UseCase useCase = useCaseModel.newUseCase(SAY_HELLO_USE_CASE);
		List<UseCaseFlow> flows = useCase.getFlows();
		
		assertEquals(1, flows.size());
		assertTrue(useCase.hasFlow(BASIC_FLOW));
		assertEquals(BASIC_FLOW, flows.iterator().next().getName());
	}

	@Test
	public void shouldCreateNoStep() {
		UseCase useCase = useCaseModel.newUseCase(SAY_HELLO_USE_CASE);

		List<UseCaseStep> steps = useCase.getSteps();
		assertEquals(0, steps.size());
	}
	
	@Test
	public void shouldCreateSingleStepWithoutActor() {		
		UseCase useCase = useCaseModel.newUseCase(SAY_HELLO_USE_CASE);
		useCase
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_TEXT).handle(EnterTextEvent.class).system(displayEnteredText());
				
		List<UseCaseStep> steps = useCase.getSteps();
		assertEquals(1, steps.size());
		
		UseCaseStep step = steps.get(0);
		assertEquals(CUSTOMER_ENTERS_TEXT, step.getName());
		assertEquals(SAY_HELLO_USE_CASE, step.getUseCase().getName());		
	}

	@Test
	public void shouldCreateSingleStepWithSingleActor() {		
		UseCase useCase = useCaseModel.newUseCase(SAY_HELLO_USE_CASE);
		useCase
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_TEXT)
					.actor(customer).handle(EnterTextEvent.class).system(displayEnteredText());
				
		List<UseCaseStep> steps = useCase.getSteps();
		assertEquals(1, steps.size());
		
		UseCaseStep step = steps.get(0);
		assertEquals(CUSTOMER_ENTERS_TEXT, step.getName());
		assertEquals(SAY_HELLO_USE_CASE, step.getUseCase().getName());		
		assertEquals(customer, step.getActorPart().getActor());
	}
	
	@Test
	public void shouldCreateSingleActor() {		
		UseCase useCase = useCaseModel.newUseCase(SAY_HELLO_USE_CASE);
		useCase
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_TEXT)
					.actor(customer).handle(EnterTextEvent.class).system(displayEnteredText());

		assertTrue(useCaseModel.hasActor(customer.getName()));
	}
	
	@Test
	public void shouldCreateSingleActorWithSingleUseCase() {		
		UseCase useCase = useCaseModel.newUseCase(SAY_HELLO_USE_CASE);
		useCase
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_TEXT)
					.actor(customer).handle(EnterTextEvent.class).system(displayEnteredText());

		Actor actorFromModel = useCaseModel.findActor(customer.getName()).get();
		
		Set<UseCase> useCases = actorFromModel.getUseCases();
		assertEquals(1, useCases.size());
		UseCase actualUseCase = useCases.iterator().next();
		assertEquals(SAY_HELLO_USE_CASE, actualUseCase.getName());
	}
	
	@Test
	public void shouldCreateSingleActorWithSingleUseCaseStep() {		
		UseCase useCase = useCaseModel.newUseCase(SAY_HELLO_USE_CASE);
		useCase
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_TEXT).actor(customer).handle(EnterTextEvent.class).system(displayEnteredText());

		Actor actorFromModel = useCaseModel.findActor(customer.getName()).get();
		List<UseCaseStep> steps = actorFromModel.getUseCaseSteps(useCase);
		
		UseCaseStep step = steps.get(0);
		assertEquals(CUSTOMER_ENTERS_TEXT, step.getName());
		assertEquals(CUSTOMER_ENTERS_TEXT, step.toString());
		assertEquals(SAY_HELLO_USE_CASE, step.getUseCase().getName());		
		assertEquals(customer, step.getActorPart().getActor());
	}
	
	@Test
	public void shouldCreateSingleStepWithNoPreviousStep() {
		UseCase useCase = useCaseModel.newUseCase(SAY_HELLO_USE_CASE);
		useCase
			.basicFlow()
				.newStep(SYSTEM_DISPLAYS_TEXT).system(displayConstantText());

		List<UseCaseStep> steps = useCase.getSteps();
		assertEquals(1, steps.size());
		
		Optional<UseCaseStep> previousStep = steps.get(0).getPreviousStep();
		
		assertFalse(previousStep.isPresent());
	}

	@Test
	public void shouldCreateTwoSteps() {
		UseCase namedUseCase = useCaseModel.newUseCase(SAY_HELLO_USE_CASE);
		
		namedUseCase
			.basicFlow()
				.newStep(SYSTEM_DISPLAYS_TEXT).system(displayConstantText()) 
				.newStep(SYSTEM_DISPLAYS_NUMBER)
					.actor(customer).handle(EnterNumberEvent.class).system(displayEnteredNumber());

		assertTrue(useCaseModel.getActors().contains(useCaseModel.getSystemActor()));
		assertTrue(useCaseModel.getActors().contains(customer));
		
		List<UseCaseStep> steps = namedUseCase.getSteps();
		assertEquals(2, steps.size());

		UseCaseStep step = steps.get(0);
		assertEquals(SYSTEM_DISPLAYS_TEXT, step.getName());
		assertEquals(SAY_HELLO_USE_CASE, step.getUseCase().getName());

		step = steps.get(1);
		assertEquals(SYSTEM_DISPLAYS_NUMBER, step.getName());
		assertEquals(SAY_HELLO_USE_CASE, step.getUseCase().getName());
	}
	
	@Test
	public void shouldCreateTwoStepsAndCheckIfTheyExistByName() {
		UseCase namedUseCase = useCaseModel.newUseCase(SAY_HELLO_USE_CASE);
		namedUseCase
			.basicFlow()
				.newStep(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
				.newStep(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());
		
		Set<UseCaseStep> steps = useCaseModel.getUseCaseSteps();
		assertEquals(2, steps.size());

		boolean firstUseCaseStepExists = namedUseCase.hasStep(SYSTEM_DISPLAYS_TEXT);
		boolean secondUseCaseStepExists = namedUseCase.hasStep(SYSTEM_DISPLAYS_TEXT_AGAIN);
		
		assertTrue(firstUseCaseStepExists);
		assertTrue(secondUseCaseStepExists);
	}
	
	@Test
	public void shouldCreateTwoStepsAndPreviousStepOfSecondOneIsFirstOne() {
		UseCase useCase = useCaseModel.newUseCase(SAY_HELLO_USE_CASE);
			
		useCase
			.basicFlow()
				.newStep(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())
				.newStep(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());

		UseCaseStep firstUseCaseStep = useCase.findStep(SYSTEM_DISPLAYS_TEXT).get();
		UseCaseStep secondUseCaseStep = useCase.findStep(SYSTEM_DISPLAYS_TEXT_AGAIN).get();
		
		assertEquals(firstUseCaseStep, secondUseCaseStep.getPreviousStep().get());
	}
	
	@Test
	public void shouldCreateTwoStepsInDifferentFlowsThatBothHaveNoPreviousSteps() { 
		UseCase useCaseInFirstFlow = useCaseModel.newUseCase(SAY_HELLO_USE_CASE);
		useCaseInFirstFlow
			.basicFlow()
				.newStep(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())	
			.newFlow("Alternative Flow")
				.newStep(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());

		UseCaseStep firstUseCaseStep = useCaseInFirstFlow.findStep(SYSTEM_DISPLAYS_TEXT).get();
		UseCaseStep secondUseCaseStep = useCaseInFirstFlow.findStep(SYSTEM_DISPLAYS_TEXT_AGAIN).get();
		
		assertFalse(firstUseCaseStep.getPreviousStep().isPresent());
		assertFalse(secondUseCaseStep.getPreviousStep().isPresent());
	}
	
	@Test
	public void shouldUniquelyIdentifyUseCasesByName() {		
		useCaseModel.newUseCase(USE_CASE)
			.basicFlow()
				.newStep(SYSTEM_DISPLAYS_TEXT).system(displayConstantText())		
			.newFlow("Alternative Flow")
				.newStep(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());		
		
		assertEquals(1, useCaseModel.getUseCases().size());
		assertEquals(useCaseModel.findUseCase(USE_CASE).get(), useCaseModel.getUseCases().iterator().next());
	}
	
	@Test
	public void shouldUniquelyIdentifyFlowsByName() {		
		useCaseModel.newUseCase(USE_CASE).newFlow(ANOTHER_FLOW);
		Optional<UseCaseFlow> existingFlow = useCaseModel.findUseCase(USE_CASE).get().findFlow(ANOTHER_FLOW);
		
		UseCase uc = useCaseModel.findUseCase(USE_CASE).get();
		assertEquals(2, uc.getFlows().size()); // This is 2 because the basic flow always exists
		assertEquals(existingFlow.get(), uc.getFlows().get(1));
	}
	
	@Test
	public void shouldUniquelyIdentifyBasicFlow() {		
		useCaseModel.newUseCase(USE_CASE)
			.basicFlow()
				.newStep(SYSTEM_DISPLAYS_TEXT).system(displayConstantText());

		useCaseModel.findUseCase(USE_CASE).get()
			.basicFlow()
				.newStep(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displayConstantText());		
		
		UseCase uc = useCaseModel.findUseCase(USE_CASE).get();
		assertEquals(1, uc.getFlows().size());
	}
	
	@Test
	public void shouldUniquelyIdentifyActorsByNameInOneUseCase() {
		useCaseModel.newUseCase(USE_CASE)
			.basicFlow()
				.newStep(CUSTOMER_ENTERS_TEXT)
					.actor(customer).handle(EnterTextEvent.class).system(displayEnteredText())
				.newStep(CUSTOMER_ENTERS_TEXT_AGAIN)
					.actor(customer).handle(EnterTextEvent.class).system(displayEnteredText());
		
		Set<UseCaseStep> steps = useCaseModel.getUseCaseSteps();
		assertEquals(2, steps.size());
		
		Iterator<UseCaseStep> stepsIt = steps.iterator();
		Actor actor1 = stepsIt.next().getActorPart().getActor();
		Actor actor2 = stepsIt.next().getActorPart().getActor();
		
		assertTrue(actor1 == actor2);
		assertEquals(customer, actor1);
	}
	
	@Test
	public void shouldUniquelyIdentifyActorsByNameInTwoUseCases() {
		useCaseModel
			.newUseCase("Use Case 1")
				.basicFlow()
					.newStep(CUSTOMER_ENTERS_TEXT)
						.actor(customer).handle(EnterTextEvent.class).system(displayEnteredText())
			.newUseCase("Use Case 2")
				.basicFlow()
					.newStep(CUSTOMER_ENTERS_TEXT_AGAIN)
						.actor(customer).handle(EnterTextEvent.class).system(displayEnteredText());		
				
		Set<UseCaseStep> steps = useCaseModel.getUseCaseSteps();
		assertEquals(2, steps.size());
		
		Iterator<UseCaseStep> stepsIt = steps.iterator();
		Actor actor1 = stepsIt.next().getActorPart().getActor();
		Actor actor2 = stepsIt.next().getActorPart().getActor();
		
		assertTrue(actor1 == actor2);
		assertEquals(customer, actor1);
	}
}
