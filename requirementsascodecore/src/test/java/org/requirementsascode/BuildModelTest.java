package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class BuildModelTest extends AbstractTestCase {
    @Before
    public void setup() {
	setupWithRecordingModelRunner();
    }

    @Test
    public void createsNoUseCase() {
	Model model = modelBuilder.build();

	Collection<UseCase> useCases = model.getUseCases();
	assertEquals(0, useCases.size());
    }

    @Test
    public void createsSingleUseCase() {
	modelBuilder.useCase(USE_CASE);

	Model model = modelBuilder.build();
	assertTrue(model.hasUseCase(USE_CASE));

	Collection<UseCase> useCases = model.getUseCases();
	assertEquals(1, useCases.size());
	assertEquals(USE_CASE, useCases.iterator().next().getName());
    }

    @Test
    public void accessesExistingUseCaseTwice() {
	modelBuilder.useCase(USE_CASE);
	modelBuilder.useCase(USE_CASE);

	Model model = modelBuilder.build();
	assertTrue(model.hasUseCase(USE_CASE));

	Collection<UseCase> useCases = model.getUseCases();
	assertEquals(1, useCases.size());
	assertEquals(USE_CASE, useCases.iterator().next().getName());
    }

    @Test
    public void createsTwoUseCasesInOneGo() {
	modelBuilder.useCase(USE_CASE);
	modelBuilder.useCase(USE_CASE_2);
	Model model = modelBuilder.build();

	Collection<UseCase> useCases = model.getUseCases();
	assertEquals(2, useCases.size());
    }

    @Test
    public void implicitlyCreatesEmptyBasicFlow() {
	modelBuilder.useCase(USE_CASE);
	Model model = modelBuilder.build();

	UseCase useCase = model.findUseCase(USE_CASE);
	Collection<Flow> flows = useCase.getFlows();

	assertEquals(1, flows.size());
	assertEquals(useCase.getBasicFlow(), flows.iterator().next());	
	assertFalse(useCase.getBasicFlow().getFirstStep().isPresent());
    }

    @Test
    public void createsNoSteps() {
	modelBuilder.useCase(USE_CASE);
	Model model = modelBuilder.build();

	UseCase useCase = model.findUseCase(USE_CASE);
	Collection<Step> steps = useCase.getSteps();
	assertEquals(0, steps.size());
    }
    
    @Test
    public void createsSingleStepThatHandlesEventWithUseCaseButWithoutFlow() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	Model model = 
		useCasePart
			.on(EntersText.class).system(displaysEnteredText())
		.build();

	UseCase useCase = useCasePart.getUseCase();
	Collection<Step> steps = useCase.getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals("S1", step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(model.getSystemActor(), step.getActors()[0]);
    }
    
    @Test
    public void createsSingleStepThatHandlesEventWithoutUseCaseAndWithoutFlow() {
	Model model = 
		modelBuilder
			.on(EntersText.class).system(displaysEnteredText())
		.build();

	Collection<Step> steps = model.getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals("S1", step.getName());
	assertEquals(model.getSystemActor(), step.getActors()[0]); 
    }
    
    @Test
    public void createsSingleStepThatPublishesEventWithoutUseCaseAndWithoutFlow() {
	Model model = 
		modelBuilder
			.on(EntersText.class).systemPublish(super::publishEnteredTextAsString)
		.build();

	Collection<Step> steps = model.getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals("S1", step.getName());
	assertEquals(model.getSystemActor(), step.getActors()[0]);
    }

    @Test
    public void createsSingleStepThatHandlesCommandWithFlow() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	Model model = 
		useCasePart.basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();

	UseCase useCase = useCasePart.getUseCase();
	assertFalse(useCase.getBasicFlow().getCondition().isPresent());
	
	Collection<Step> steps = useCase.getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals(CUSTOMER_ENTERS_TEXT, step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(model.getUserActor(), step.getActors()[0]);
    }
    
    @Test
    public void createsSingleStepThatHandlesCommandWithFlowAndPublishesEvent() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	Model model = 
		useCasePart.basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).systemPublish(super::publishEnteredTextAsString)
		.build();

	UseCase useCase = useCasePart.getUseCase();
	assertFalse(useCase.getBasicFlow().getCondition().isPresent());
	
	Collection<Step> steps = useCase.getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals(CUSTOMER_ENTERS_TEXT, step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(model.getUserActor(), step.getActors()[0]);
    }
    
    @Test
    public void createsSingleStepThatHandlesCommandWithCondition() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	useCasePart.basicFlow().condition(() -> textIsAvailable())
		.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
	.build();

	UseCase useCase = useCasePart.getUseCase();
	assertTrue(useCase.getBasicFlow().getCondition().isPresent());
    }

    @Test
    public void createsSingleStepThatHandlesEventWithFlow() {
	Model model = 
		modelBuilder.useCase(USE_CASE).basicFlow()
			.step(SYSTEM_DISPLAYS_TEXT).on(EntersText.class).system(displaysEnteredText())
		.build();

	UseCase useCase = model.findUseCase(USE_CASE);
	Collection<Step> steps = useCase.getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals(SYSTEM_DISPLAYS_TEXT, step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(model.getSystemActor(), step.getActors()[0]);
    }
    
    @Test
    public void createsSingleStepThatHandlesEventWithFlowAndPublishesEvent() {
	Model model = 
		modelBuilder.useCase(USE_CASE).basicFlow()
			.step(SYSTEM_DISPLAYS_TEXT).on(EntersText.class).systemPublish(super::publishEnteredTextAsString)
		.build();

	UseCase useCase = model.findUseCase(USE_CASE);
	Collection<Step> steps = useCase.getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals(SYSTEM_DISPLAYS_TEXT, step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(model.getSystemActor(), step.getActors()[0]);
    }

    @Test
    public void createsSingleStepThatPerformsSystemReactionAutomatically() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	Model model = 
		useCasePart.basicFlow()
			.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
		.build();

	Collection<Step> steps = useCasePart.getUseCase().getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals(SYSTEM_DISPLAYS_TEXT, step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(model.getSystemActor(), step.getActors()[0]);
    }
    
    @Test
    public void createsSingleStepThatPerformsSystemReactionAutomaticallyAndPublishesEvent() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	Model model = 
		useCasePart.basicFlow()
			.step(SYSTEM_DISPLAYS_TEXT).systemPublish(publishConstantText())
		.build();

	Collection<Step> steps = useCasePart.getUseCase().getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals(SYSTEM_DISPLAYS_TEXT, step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(model.getSystemActor(), step.getActors()[0]);
    }

    @Test
    public void createsSingleStepThatPerformsSystemReactionAutomaticallyForSpecificActor() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	Model model = 
		useCasePart.basicFlow()
			.step(SYSTEM_DISPLAYS_TEXT).as(customer).system(displaysConstantText())
		.build();

	Collection<Step> steps = useCasePart.getUseCase().getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals(SYSTEM_DISPLAYS_TEXT, step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertTrue(model.hasActor(customer.getName()));
	assertEquals(customer, step.getActors()[0]);
    }

    @Test
    public void accessesExistingCustomerActor() {
	Actor actualCustomer = modelBuilder.actor(CUSTOMER);
	assertEquals(customer, actualCustomer);
    }

    @Test
    public void createsSingleActorWithSingleUseCase() {
	Model model = 
		modelBuilder.useCase(USE_CASE).basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).as(customer).user(EntersText.class).system(displaysEnteredText())
		.build();

	Actor customerActor = model.findActor(CUSTOMER);
	Set<UseCase> useCases = customerActor.getUseCases();
	assertEquals(1, useCases.size());

	UseCase actualUseCase = useCases.iterator().next();
	assertEquals(USE_CASE, actualUseCase.getName());
    }
    
    @Test
    public void createsSingleDefaultActorWithSingleUseCase() {
	Model model = 
		modelBuilder.useCase(USE_CASE).as(customer).basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();

	Actor customerActor = model.findActor(CUSTOMER);
	Set<UseCase> useCases = customerActor.getUseCases();
	assertEquals(1, useCases.size());

	UseCase actualUseCase = useCases.iterator().next();
	assertEquals(USE_CASE, actualUseCase.getName());
    }

    @Test
    public void createsSingleActorWithSingleUseCaseStep() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	Model model = 
		useCasePart.basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).as(customer).user(EntersText.class).system(displaysEnteredText())
		.build();

	Actor customerActor = model.findActor(customer.getName());
	List<Step> steps = customerActor.getStepsOf(useCasePart.getUseCase());
	Step step = steps.get(0);

	assertEquals(CUSTOMER_ENTERS_TEXT, step.getName());
	assertEquals(CUSTOMER_ENTERS_TEXT, step.toString());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(customer, step.getActors()[0]);
    }

    @Test
    public void createsTwoActorsWithSingleUseCaseStep() {
	Actor anotherActor = modelBuilder.actor("Another Actor");
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	Model model = 
		useCasePart.basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).as(customer, anotherActor).user(EntersText.class).system(displaysEnteredText())
		.build();

	Actor customerActor = model.findActor(customer.getName());
	List<Step> steps = customerActor.getStepsOf(useCasePart.getUseCase());
	Step step = steps.get(0);

	assertEquals(CUSTOMER_ENTERS_TEXT, step.getName());
	assertEquals(CUSTOMER_ENTERS_TEXT, step.toString());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(customer, step.getActors()[0]);

	steps = anotherActor.getStepsOf(useCasePart.getUseCase());
	step = steps.get(0);

	assertEquals(CUSTOMER_ENTERS_TEXT, step.getName());
	assertEquals(anotherActor, step.getActors()[1]);
    }

    @Test
    public void createsSingleStepWithNoPreviousStep() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	useCasePart.basicFlow()
		.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText());

	Collection<Step> steps = useCasePart.getUseCase().getSteps();
	assertEquals(1, steps.size());

	Optional<FlowStep> previousStep = ((FlowStep) steps.iterator().next()).getPreviousStepInFlow();

	assertFalse(previousStep.isPresent());
    }
    
    @Test
    public void createsTwoStepsWithoutFlow() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	useCasePart
		.on(EntersText.class).system(displaysEnteredText())
		.on(EntersNumber.class).system(displaysEnteredNumber())
	.build();

	Collection<Step> steps = useCasePart.getUseCase().getSteps();
	assertEquals(2, steps.size());

	Iterator<Step> stepIt = steps.iterator();
	Step step = stepIt.next();
	assertEquals("S1", step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());

	step = stepIt.next();
	assertEquals("S2", step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
    }

    @Test
    public void createsTwoStepsWithActorAndFlow() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	Model model = 
		useCasePart.basicFlow()
			.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
			.step(SYSTEM_DISPLAYS_NUMBER).as(customer).user(EntersNumber.class).system(displaysEnteredNumber())
		.build();

	assertTrue(model.getActors().contains(model.getSystemActor()));
	assertTrue(model.getActors().contains(customer));

	Collection<Step> steps = useCasePart.getUseCase().getSteps();
	assertEquals(2, steps.size());

	Iterator<Step> stepIt = steps.iterator();
	Step step = stepIt.next();
	assertEquals(SYSTEM_DISPLAYS_TEXT, step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());

	step = stepIt.next();
	assertEquals(SYSTEM_DISPLAYS_NUMBER, step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
    }

    @Test
    public void createsTwoStepsAndCheckIfTheyExistInUseCaseByName() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	useCasePart.basicFlow()
		.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
		.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText());

	Collection<Step> steps = useCasePart.getUseCase().getSteps();
	assertEquals(2, steps.size());

	boolean firstUseCaseStepExists = useCasePart.getUseCase().hasStep(SYSTEM_DISPLAYS_TEXT);
	boolean secondUseCaseStepExists = useCasePart.getUseCase().hasStep(SYSTEM_DISPLAYS_TEXT_AGAIN);

	assertTrue(firstUseCaseStepExists);
	assertTrue(secondUseCaseStepExists);
    }

    @Test
    public void createsTwoStepsInBasicFlowAndCheckIfTheyExistByIndex() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	useCasePart.basicFlow()
		.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
		.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText());

	List<FlowStep> steps = useCasePart.getUseCase().getBasicFlow().getSteps();
	assertEquals(2, steps.size());

	assertEquals(SYSTEM_DISPLAYS_TEXT, steps.get(0).getName());
	assertEquals(SYSTEM_DISPLAYS_TEXT_AGAIN, steps.get(1).getName());
    }

    @Test
    public void createsOneStepInAlternativeFlowAndCheckIfItExistsByIndex() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	useCasePart.basicFlow()
		.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText()).flow(ALTERNATIVE_FLOW)
		.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText());

	List<FlowStep> steps = useCasePart.getUseCase().findFlow(ALTERNATIVE_FLOW).getSteps();
	assertEquals(1, steps.size());

	assertEquals(SYSTEM_DISPLAYS_TEXT_AGAIN, steps.get(0).getName());
    }

    @Test
    public void createsTwoStepsAndPreviousStepOfSecondOneIsFirstOne() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	useCasePart.basicFlow()
		.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
		.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText());

	FlowStep firstUseCaseStep = (FlowStep)useCasePart.getUseCase().findStep(SYSTEM_DISPLAYS_TEXT);
	FlowStep secondUseCaseStep = (FlowStep)useCasePart.getUseCase().findStep(SYSTEM_DISPLAYS_TEXT_AGAIN);

	assertEquals(firstUseCaseStep, secondUseCaseStep.getPreviousStepInFlow().get());
    }

    @Test
    public void createsTwoStepsIncrementally() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);
	modelBuilder.useCase(USE_CASE).basicFlow().step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText());

	useCasePart = modelBuilder.useCase(USE_CASE);
	modelBuilder.useCase(USE_CASE).basicFlow()
		.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText());

	FlowStep firstUseCaseStep = (FlowStep)useCasePart.getUseCase().findStep(SYSTEM_DISPLAYS_TEXT);
	FlowStep secondUseCaseStep = (FlowStep)useCasePart.getUseCase().findStep(SYSTEM_DISPLAYS_TEXT_AGAIN);

	assertEquals(firstUseCaseStep, secondUseCaseStep.getPreviousStepInFlow().get());
    }

    @Test
    public void createsTwoStepsInDifferentFlowsThatBothHaveNoPreviousSteps() {
	UseCasePart useCasePart = modelBuilder.useCase(USE_CASE);

	useCasePart
		.basicFlow()
			.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
		.flow(ALTERNATIVE_FLOW)
			.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText());

	FlowStep firstUseCaseStep = (FlowStep)useCasePart.getUseCase().findStep(SYSTEM_DISPLAYS_TEXT);
	FlowStep secondUseCaseStep = (FlowStep)useCasePart.getUseCase().findStep(SYSTEM_DISPLAYS_TEXT_AGAIN);

	assertFalse(firstUseCaseStep.getPreviousStepInFlow().isPresent());
	assertFalse(secondUseCaseStep.getPreviousStepInFlow().isPresent());
    }

    @Test
    public void useCasesAreUniquelyIdentifiedByName() {
	Model model = 
		modelBuilder.useCase(USE_CASE).basicFlow()
			.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
		.flow(ALTERNATIVE_FLOW)
			.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText()).build();

	assertEquals(1, model.getUseCases().size());
	assertEquals(model.findUseCase(USE_CASE), model.getUseCases().iterator().next());
    }

    @Test
    public void flowsAreUniquelyIdentifiedByName() {
	UseCase useCase = modelBuilder.useCase(USE_CASE).getUseCase();
	useCase.newFlow(ALTERNATIVE_FLOW);
	Flow existingFlow = useCase.findFlow(ALTERNATIVE_FLOW);

	assertEquals(2, useCase.getFlows().size()); // This is 2 because the basic flow always exists

	Iterator<Flow> flowIt = useCase.getFlows().iterator();
	assertEquals(useCase.getBasicFlow(), flowIt.next());
	assertEquals(existingFlow, flowIt.next());
    }

    @Test
    public void thereIsOnlyOneBasicFlowPerUseCase() {
	modelBuilder.useCase(USE_CASE);

	Model model = modelBuilder.build();
	UseCase uc = model.findUseCase(USE_CASE);
	assertEquals(1, uc.getFlows().size());
    }

    @Test
    public void actorsCanBeReusedInUseCase() {
	Model model = 
		modelBuilder.useCase(USE_CASE).basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).as(customer).user(EntersText.class).system(displaysEnteredText())
			.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(customer).user(EntersText.class).system(displaysEnteredText())
		.build();

	Collection<Step> steps = model.getSteps();
	assertEquals(2, steps.size());

	Iterator<Step> stepsIt = steps.iterator();
	Actor actor1 = stepsIt.next().getActors()[0];
	Actor actor2 = stepsIt.next().getActors()[0];

	assertTrue(actor1 == actor2);
	assertEquals(customer, actor1);
    }

    @Test
    public void actorsCanBeReusedBetweenUseCases() {
	Model model = 
		modelBuilder.useCase(USE_CASE).basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).as(customer).user(EntersText.class).system(displaysEnteredText())
		.useCase(USE_CASE_2)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(customer).user(EntersText.class).system(displaysEnteredText())
		.build();

	Collection<Step> steps = model.getSteps();
	assertEquals(2, steps.size());

	Iterator<Step> stepsIt = steps.iterator();
	Actor actor1 = stepsIt.next().getActors()[0];
	Actor actor2 = stepsIt.next().getActors()[0];

	assertTrue(actor1 == actor2);
	assertEquals(customer, actor1);
    }
    
    @Test
    public void defaultActorIsUsedForSeveralSteps() {
	Model model = 
		modelBuilder.useCase(USE_CASE).as(customer).basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
			.step(CUSTOMER_ENTERS_TEXT_AGAIN).user(EntersText.class).system(displaysEnteredText())
		.build();

	Collection<Step> steps = model.getSteps();
	assertEquals(2, steps.size());

	Iterator<Step> stepsIt = steps.iterator();
	Actor actor1 = stepsIt.next().getActors()[0];
	Actor actor2 = stepsIt.next().getActors()[0];

	assertTrue(actor1 == actor2);
	assertEquals(customer, actor1);
    }
}
