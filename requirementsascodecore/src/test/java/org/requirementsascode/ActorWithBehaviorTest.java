package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class ActorWithBehaviorTest extends AbstractTestCase{
	
	private String publishedString;

	@Before
	public void setup() {
		setupWithRecordingModelRunner();
	}
	
  @Test
  public void actorDoesntDoAnything() {
  	Optional<Object> latestPublishedEvent = customer.reactTo(entersText());
  	assertFalse(customer.getBehavior().isPresent());
		assertTrue(latestPublishedEvent.isEmpty());
  }
	
  @Test
  public void actorReactsToEvent() {
		Model model = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).system(displaysEnteredText())
		.build();
	
		customer.withBehavior(model).reactTo(entersText());
  	assertTrue(customer.getBehavior().isPresent());

		Optional<Step> latestStepRun = customer.getModelRunner().flatMap(mr -> mr.getLatestStep());
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
  }
  
  @Test
  public void actorReactsToTwoEvents() {
		Model model = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).system(displaysEnteredText())
			.step(CUSTOMER_ENTERS_NUMBER).on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		customer.withBehavior(model).reactTo(entersText());
		Optional<Step> latestStepRun = customer.getModelRunner().flatMap(mr -> mr.getLatestStep());
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
	
		customer.reactTo(entersNumber());
		latestStepRun = customer.getModelRunner().flatMap(mr -> mr.getLatestStep());
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
  }
  
  @Test
  public void actorReactsIfActorInModelMatches() {
  	Actor validActor = new Actor("ValidActor");
  	
		Model model = modelBuilder.useCase(USE_CASE).as(validActor)
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();
	
		customer.withBehavior(model).reactTo(entersText(), validActor);

		Optional<Step> latestStepRun = customer.getModelRunner().flatMap(mr -> mr.getLatestStep());
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
  }
  
  @Test
  public void actorDoesnReactIfActorInModelIsDifferent() {
  	Actor validActor = new Actor("ValidActor");
  	Actor invalidActor = new Actor("InvalidActor");

		Model model = modelBuilder.useCase(USE_CASE).as(validActor)
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();
	
		customer.withBehavior(model).reactTo(entersText(), invalidActor);

		Optional<Step> latestStepRun = invalidActor.getModelRunner().flatMap(mr -> mr.getLatestStep());
		assertFalse(latestStepRun.isPresent());
  }
  
  @Test
  public void actorReturnsCorrectPublishedEvent() {
		Model model = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).systemPublish(publishEnteredTextAsString())
		.build();

		String publishedString = (String) customer.withBehavior(model).reactTo(entersText()).get();
		assertEquals(TEXT, publishedString);
	}
  
  @Test
  public void actorModelRunnerIsConfigurable() {
		Model model = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).systemPublish(publishEnteredTextAsString())
		.build();
	
		customer.withBehavior(model);		
		Optional<ModelRunner>	 customerRunner = customer.getModelRunner();
		
		customerRunner.map(cr -> cr.publishWith(msg -> {
			publishedString = (String)msg;
		}));
		
		publishedString = null;
		customer.reactTo(entersText());
		assertEquals(TEXT, publishedString);
  }
  
  @Test
  public void twoActorsInteract() {
		Model model2 = Model.builder()
			.step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).system(displaysEnteredText())
		.build();
		partner2.withBehavior(model2);
		
		Model model1 = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).systemPublish(et -> et).to(partner2)
		.build();
		partner.withBehavior(model1).reactTo(entersText());

		Optional<Step> latestStepRun = partner2.getModelRunner().flatMap(mr -> mr.getLatestStep());
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
  }
}
