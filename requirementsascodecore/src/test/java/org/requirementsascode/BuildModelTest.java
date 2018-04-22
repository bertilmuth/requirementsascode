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
	setupWith(new TestUseCaseModelRunner());
    }

    @Test
    public void createsNoUseCase() {
	UseCaseModel useCaseModel = useCaseModelBuilder.build();

	Collection<UseCase> useCases = useCaseModel.getUseCases();
	assertEquals(0, useCases.size());
    }

    @Test
    public void createsSingleUseCase() {
	useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = useCaseModelBuilder.build();
	assertTrue(useCaseModel.hasUseCase(USE_CASE));

	Collection<UseCase> useCases = useCaseModel.getUseCases();
	assertEquals(1, useCases.size());
	assertEquals(USE_CASE, useCases.iterator().next().getName());
    }

    @Test
    public void accessesExistingUseCaseTwice() {
	useCaseModelBuilder.useCase(USE_CASE);
	useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = useCaseModelBuilder.build();
	assertTrue(useCaseModel.hasUseCase(USE_CASE));

	Collection<UseCase> useCases = useCaseModel.getUseCases();
	assertEquals(1, useCases.size());
	assertEquals(USE_CASE, useCases.iterator().next().getName());
    }

    @Test
    public void createsTwoUseCasesInOneGo() {
	useCaseModelBuilder.useCase(USE_CASE);
	useCaseModelBuilder.useCase(USE_CASE_2);
	UseCaseModel useCaseModel = useCaseModelBuilder.build();

	Collection<UseCase> useCases = useCaseModel.getUseCases();
	assertEquals(2, useCases.size());
    }

    @Test
    public void implicitlyCreatesBasicFlow() {
	useCaseModelBuilder.useCase(USE_CASE);
	UseCaseModel useCaseModel = useCaseModelBuilder.build();

	UseCase useCase = useCaseModel.findUseCase(USE_CASE);
	Collection<Flow> flows = useCase.getFlows();

	assertEquals(1, flows.size());
    }

    @Test
    public void createsNoSteps() {
	useCaseModelBuilder.useCase(USE_CASE);
	UseCaseModel useCaseModel = useCaseModelBuilder.build();

	UseCase useCase = useCaseModel.findUseCase(USE_CASE);
	Collection<Step> steps = useCase.getSteps();
	assertEquals(0, steps.size());
    }
    
    @Test
    public void createsSingleStepThatHandlesUserCommandWithUseCaseButWithoutFlow() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = 
		useCasePart
			.handles(EntersText.class).with(displaysEnteredText())
		.build();

	UseCase useCase = useCasePart.useCase();
	Collection<Step> steps = useCase.getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals("S1", step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(useCaseModel.getSystemActor(), step.getActors()[0]);
    }
    
    @Test
    public void createsSingleStepThatHandlesUserCommandWithoutUseCaseAndWithoutFlow() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = 
		useCasePart
			.handles(EntersText.class).with(displaysEnteredText())
		.build();

	UseCase useCase = useCasePart.useCase();
	Collection<Step> steps = useCase.getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals("S1", step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(useCaseModel.getSystemActor(), step.getActors()[0]);
    }

    @Test
    public void createsSingleStepThatHandlesUserCommandWithFlow() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = 
		useCasePart.basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();

	UseCase useCase = useCasePart.useCase();
	Collection<Step> steps = useCase.getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals(CUSTOMER_ENTERS_TEXT, step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(useCaseModel.getUserActor(), step.getActors()[0]);
    }

    @Test
    public void createsSingleStepThatHandlesSystemEvent() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = 
		useCasePart.basicFlow()
			.step(SYSTEM_DISPLAYS_TEXT).handles(EntersText.class).system(displaysEnteredText())
		.build();

	UseCase useCase = useCaseModel.findUseCase(USE_CASE);
	Collection<Step> steps = useCase.getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals(SYSTEM_DISPLAYS_TEXT, step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(useCaseModel.getSystemActor(), step.getActors()[0]);
    }

    @Test
    public void createsSingleStepThatPerformsSystemReactionAutomatically() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = 
		useCasePart.basicFlow()
			.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
		.build();

	Collection<Step> steps = useCasePart.useCase().getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals(SYSTEM_DISPLAYS_TEXT, step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(useCaseModel.getSystemActor(), step.getActors()[0]);
    }

    @Test
    public void createsSingleStepThatPerformsSystemReactionAutomaticallyForSpecificActor() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = 
		useCasePart.basicFlow()
			.step(SYSTEM_DISPLAYS_TEXT).as(customer).system(displaysConstantText())
		.build();

	Collection<Step> steps = useCasePart.useCase().getSteps();
	assertEquals(1, steps.size());

	Step step = steps.iterator().next();
	assertEquals(SYSTEM_DISPLAYS_TEXT, step.getName());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertTrue(useCaseModel.hasActor(customer.getName()));
	assertEquals(customer, step.getActors()[0]);
    }

    @Test
    public void accessesExistingCustomerActor() {
	Actor actualCustomer = useCaseModelBuilder.actor(CUSTOMER);
	assertEquals(customer, actualCustomer);
    }

    @Test
    public void createsSingleActorWithSingleUseCase() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = 
		useCasePart.basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).as(customer).user(EntersText.class).system(displaysEnteredText())
		.build();

	Actor customerActor = useCaseModel.findActor(CUSTOMER);
	Set<UseCase> useCases = customerActor.getUseCases();
	assertEquals(1, useCases.size());

	UseCase actualUseCase = useCases.iterator().next();
	assertEquals(USE_CASE, actualUseCase.getName());
    }

    @Test
    public void createsSingleActorWithSingleUseCaseStep() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = 
		useCasePart.basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).as(customer).user(EntersText.class).system(displaysEnteredText())
		.build();

	Actor customerActor = useCaseModel.findActor(customer.getName());
	List<Step> steps = customerActor.getStepsOf(useCasePart.useCase());
	Step step = steps.get(0);

	assertEquals(CUSTOMER_ENTERS_TEXT, step.getName());
	assertEquals(CUSTOMER_ENTERS_TEXT, step.toString());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(customer, step.getActors()[0]);
    }

    @Test
    public void createsTwoActorsWithSingleUseCaseStep() {
	Actor anotherActor = useCaseModelBuilder.actor("Another Actor");
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = 
		useCasePart.basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).as(customer, anotherActor).user(EntersText.class).system(displaysEnteredText())
		.build();

	Actor customerActor = useCaseModel.findActor(customer.getName());
	List<Step> steps = customerActor.getStepsOf(useCasePart.useCase());
	Step step = steps.get(0);

	assertEquals(CUSTOMER_ENTERS_TEXT, step.getName());
	assertEquals(CUSTOMER_ENTERS_TEXT, step.toString());
	assertEquals(USE_CASE, step.getUseCase().getName());
	assertEquals(customer, step.getActors()[0]);

	steps = anotherActor.getStepsOf(useCasePart.useCase());
	step = steps.get(0);

	assertEquals(CUSTOMER_ENTERS_TEXT, step.getName());
	assertEquals(anotherActor, step.getActors()[1]);
    }

    @Test
    public void createsSingleStepWithNoPreviousStep() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	useCasePart.basicFlow()
		.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText());

	Collection<Step> steps = useCasePart.useCase().getSteps();
	assertEquals(1, steps.size());

	Optional<FlowStep> previousStep = ((FlowStep) steps.iterator().next()).getPreviousStepInFlow();

	assertFalse(previousStep.isPresent());
    }
    
    @Test
    public void createsTwoStepsWithoutFlow() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	useCasePart
		.handles(EntersText.class).with(displaysEnteredText())
		.handles(EntersNumber.class).with(displaysEnteredNumber())
	.build();

	Collection<Step> steps = useCasePart.useCase().getSteps();
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
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = 
		useCasePart.basicFlow()
			.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
			.step(SYSTEM_DISPLAYS_NUMBER).as(customer).user(EntersNumber.class).system(displaysEnteredNumber())
		.build();

	assertTrue(useCaseModel.getActors().contains(useCaseModel.getSystemActor()));
	assertTrue(useCaseModel.getActors().contains(customer));

	Collection<Step> steps = useCasePart.useCase().getSteps();
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
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	useCasePart.basicFlow()
		.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
		.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText());

	Collection<Step> steps = useCasePart.useCase().getSteps();
	assertEquals(2, steps.size());

	boolean firstUseCaseStepExists = useCasePart.useCase().hasStep(SYSTEM_DISPLAYS_TEXT);
	boolean secondUseCaseStepExists = useCasePart.useCase().hasStep(SYSTEM_DISPLAYS_TEXT_AGAIN);

	assertTrue(firstUseCaseStepExists);
	assertTrue(secondUseCaseStepExists);
    }

    @Test
    public void createsTwoStepsInBasicFlowAndCheckIfTheyExistByIndex() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	useCasePart.basicFlow()
		.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
		.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText());

	List<FlowStep> steps = useCasePart.useCase().getBasicFlow().getSteps();
	assertEquals(2, steps.size());

	assertEquals(SYSTEM_DISPLAYS_TEXT, steps.get(0).getName());
	assertEquals(SYSTEM_DISPLAYS_TEXT_AGAIN, steps.get(1).getName());
    }

    @Test
    public void createsOneStepInAlternativeFlowAndCheckIfItExistsByIndex() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	useCasePart.basicFlow()
		.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText()).flow(ALTERNATIVE_FLOW)
		.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText());

	List<FlowStep> steps = useCasePart.useCase().findFlow(ALTERNATIVE_FLOW).getSteps();
	assertEquals(1, steps.size());

	assertEquals(SYSTEM_DISPLAYS_TEXT_AGAIN, steps.get(0).getName());
    }

    @Test
    public void createsTwoStepsAndPreviousStepOfSecondOneIsFirstOne() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	useCasePart.basicFlow()
		.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
		.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText());

	FlowStep firstUseCaseStep = (FlowStep)useCasePart.useCase().findStep(SYSTEM_DISPLAYS_TEXT);
	FlowStep secondUseCaseStep = (FlowStep)useCasePart.useCase().findStep(SYSTEM_DISPLAYS_TEXT_AGAIN);

	assertEquals(firstUseCaseStep, secondUseCaseStep.getPreviousStepInFlow().get());
    }

    @Test
    public void createsTwoStepsIncrementally() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);
	useCaseModelBuilder.useCase(USE_CASE).basicFlow().step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText());

	useCasePart = useCaseModelBuilder.useCase(USE_CASE);
	useCaseModelBuilder.useCase(USE_CASE).basicFlow()
		.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText());

	FlowStep firstUseCaseStep = (FlowStep)useCasePart.useCase().findStep(SYSTEM_DISPLAYS_TEXT);
	FlowStep secondUseCaseStep = (FlowStep)useCasePart.useCase().findStep(SYSTEM_DISPLAYS_TEXT_AGAIN);

	assertEquals(firstUseCaseStep, secondUseCaseStep.getPreviousStepInFlow().get());
    }

    @Test
    public void createsTwoStepsInDifferentFlowsThatBothHaveNoPreviousSteps() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	useCasePart
		.basicFlow()
			.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
		.flow(ALTERNATIVE_FLOW)
			.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText());

	FlowStep firstUseCaseStep = (FlowStep)useCasePart.useCase().findStep(SYSTEM_DISPLAYS_TEXT);
	FlowStep secondUseCaseStep = (FlowStep)useCasePart.useCase().findStep(SYSTEM_DISPLAYS_TEXT_AGAIN);

	assertFalse(firstUseCaseStep.getPreviousStepInFlow().isPresent());
	assertFalse(secondUseCaseStep.getPreviousStepInFlow().isPresent());
    }

    @Test
    public void useCasesAreUniquelyIdentifiedByName() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = 
		useCasePart.basicFlow()
			.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
		.flow(ALTERNATIVE_FLOW)
			.step(SYSTEM_DISPLAYS_TEXT_AGAIN).system(displaysConstantText()).build();

	assertEquals(1, useCaseModel.getUseCases().size());
	assertEquals(useCaseModel.findUseCase(USE_CASE), useCaseModel.getUseCases().iterator().next());
    }

    @Test
    public void flowsAreUniquelyIdentifiedByName() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	UseCase useCase = useCasePart.useCase();
	useCase.newFlow(ALTERNATIVE_FLOW);
	Flow existingFlow = useCase.findFlow(ALTERNATIVE_FLOW);

	assertEquals(2, useCase.getFlows().size()); // This is 2 because the basic flow always exists

	Iterator<Flow> flowIt = useCase.getFlows().iterator();
	assertEquals(useCase.getBasicFlow(), flowIt.next());
	assertEquals(existingFlow, flowIt.next());
    }

    @Test
    public void thereIsOnlyOneBasicFlowPerUseCase() {
	useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = useCaseModelBuilder.build();
	UseCase uc = useCaseModel.findUseCase(USE_CASE);
	assertEquals(1, uc.getFlows().size());
    }

    @Test
    public void actorsCanBeReusedInUseCase() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = 
		useCasePart.basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).as(customer).user(EntersText.class).system(displaysEnteredText())
			.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(customer).user(EntersText.class).system(displaysEnteredText())
		.build();

	Collection<Step> steps = useCaseModel.getSteps();
	assertEquals(2, steps.size());

	Iterator<Step> stepsIt = steps.iterator();
	Actor actor1 = stepsIt.next().getActors()[0];
	Actor actor2 = stepsIt.next().getActors()[0];

	assertTrue(actor1 == actor2);
	assertEquals(customer, actor1);
    }

    @Test
    public void actorsCanBeReusedBetweenUseCases() {
	UseCasePart useCasePart = useCaseModelBuilder.useCase(USE_CASE);

	UseCaseModel useCaseModel = 
		useCasePart.basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).as(customer).user(EntersText.class).system(displaysEnteredText())
		.useCase(USE_CASE_2)
			.basicFlow()
				.step(CUSTOMER_ENTERS_TEXT_AGAIN).as(customer).user(EntersText.class).system(displaysEnteredText())
		.build();

	Collection<Step> steps = useCaseModel.getSteps();
	assertEquals(2, steps.size());

	Iterator<Step> stepsIt = steps.iterator();
	Actor actor1 = stepsIt.next().getActors()[0];
	Actor actor2 = stepsIt.next().getActors()[0];

	assertTrue(actor1 == actor2);
	assertEquals(customer, actor1);
    }
}
