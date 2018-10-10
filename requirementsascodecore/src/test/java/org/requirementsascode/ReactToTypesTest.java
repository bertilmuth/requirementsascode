package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ReactToTypesTest extends AbstractTestCase {

    @Before
    public void setup() {
	setupWith(new TestModelRunner());
    }

    @Test
    public void noEventsReactedToIfNotRunning() {
	Set<Class<?>> reactToTypes = modelRunner.getReactToTypes();
	assertEquals(0, reactToTypes.size());
    }

    @Test
    public void noEventsReactedToInEmptyModel() {
	Model model = modelBuilder.useCase(USE_CASE).build();

	modelRunner.run(model);

	Set<Class<?>> reactToTypes = modelRunner.getReactToTypes();
	assertEquals(0, reactToTypes.size());
    }
    

    @Test
    public void singleEventTypeReactedTo() {
	Model model = modelBuilder.useCase(USE_CASE)
		.basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText()).build();

	modelRunner.run(model);

	Set<Class<?>> reactToTypes = modelRunner.getReactToTypes();
	assertEquals(1, reactToTypes.size());

	Class<?> eventTypeReactedTo = reactToTypes.iterator().next();
	assertEquals(EntersText.class, eventTypeReactedTo);
    }

    @Test
    public void sameEventTypeReactedTo() {
	Model model = modelBuilder.useCase(USE_CASE)
		.basicFlow().anytime()
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.flow("Alternative Flow: Could react as well").anytime()
			.step(CUSTOMER_ENTERS_ALTERNATIVE_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();

	modelRunner.run(model);

	Set<Class<?>> reactToTypes = modelRunner.getReactToTypes();
	assertEquals(1, reactToTypes.size());

	Class<?> eventTypeReactedTo = reactToTypes.iterator().next();
	assertEquals(EntersText.class, eventTypeReactedTo);
    }
    
    @Test
    public void differentEventTypesReactedTo() {
	Model model = modelBuilder.useCase(USE_CASE)
		.basicFlow().anytime()
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.flow("Alternative Flow: Could react as well").anytime()
			.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
		.build();

	modelRunner.run(model);

	Set<Class<?>> reactToTypes = modelRunner.getReactToTypes();
	assertEquals(2, reactToTypes.size());

	assertTrue(reactToTypes.contains(EntersText.class));
	assertTrue(reactToTypes.contains(EntersNumber.class));
    }
    
    @Test
    public void eventTypesReactedOnlyIfConditionFulfilled() {
	Model model = modelBuilder.useCase(USE_CASE)
		.basicFlow().condition(() -> false)
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.flow("Alternative Flow: Could react as well").anytime()
			.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
		.build();

	modelRunner.run(model);

	Set<Class<?>> reactToTypes = modelRunner.getReactToTypes();
	assertEquals(1, reactToTypes.size());

	Class<?> eventTypeReactedTo = reactToTypes.iterator().next();
	assertEquals(EntersNumber.class, eventTypeReactedTo);
    }
    
    @Test
    public void eventTypesReactedOnlyIfFlowPositionIsRight() {
	Model model = modelBuilder.useCase(USE_CASE)
		.basicFlow()
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.flow("Alternative Flow: Could react as well").after(CUSTOMER_ENTERS_TEXT)
			.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
		.build();

	modelRunner.run(model);

	Set<Class<?>> reactToTypes = modelRunner.getReactToTypes();
	assertEquals(1, reactToTypes.size());

	Class<?> eventTypeReactedTo = reactToTypes.iterator().next();
	assertEquals(EntersText.class, eventTypeReactedTo);
	
	modelRunner.reactTo(entersText());
	
	reactToTypes = modelRunner.getReactToTypes();
	assertEquals(1, reactToTypes.size());

	eventTypeReactedTo = reactToTypes.iterator().next();
	assertEquals(EntersNumber.class, eventTypeReactedTo);
    }
}
