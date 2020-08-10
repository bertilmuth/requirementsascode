package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
  	assertNull(customer.behavior());
		assertFalse(latestPublishedEvent.isPresent());
  }
	
  @Test
  public void actorReactsToEvent() {
		Model model = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).system(displaysEnteredText())
		.build();
	
		customer.withBehavior(model).reactTo(entersText());
  	assertNotNull(customer.behavior());

		Optional<Step> latestStepRun = customer.getModelRunner().getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
  }
  
  @Test
  public void actorReactsToTwoEvents() {
		Model model = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).system(displaysEnteredText())
			.step(CUSTOMER_ENTERS_NUMBER).on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		customer.withBehavior(model).reactTo(entersText());
		Optional<Step> latestStepRun = customer.getModelRunner().getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
	
		customer.reactTo(entersNumber());
		latestStepRun = customer.getModelRunner().getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
  }
  
  @Test
  public void actorReactsIfActorInModelMatches() {
  	Actor validActor = new Actor("ValidActor");
  	
		Model model = modelBuilder.useCase(USE_CASE).as(validActor)
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();
	
		customer.withBehavior(model).reactTo(entersText(), validActor);

		Optional<Step> latestStepRun = customer.getModelRunner().getLatestStep();
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

		Optional<Step> latestStepRun = invalidActor.getModelRunner().getLatestStep();
		assertFalse(latestStepRun.isPresent());
  }
  
  @Test
  public void actorReactsIfActorInModelMatchesForSecondEvent() {
    Actor validActor = new Actor("ValidActor");
    Actor invalidActor = new Actor("InvalidActor");

    Model model = modelBuilder.useCase(USE_CASE).as(validActor)
      .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
    .build();
  
    Actor customerWithBehavior = customer.withBehavior(model);
    customerWithBehavior.reactTo(entersText(), invalidActor);
    customerWithBehavior.reactTo(entersText(), validActor);

    Optional<Step> latestStepRun = customer.getModelRunner().getLatestStep();
    assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
  }
  
  @Test
  public void actorReturnsCorrectPublishedEvent() {
		Model model = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).systemPublish(publishEnteredTextAsString())
		.build();

		Optional<String> optionalString = customer.withBehavior(model).reactTo(entersText());
		String publishedString = optionalString.get();
		assertEquals(TEXT, publishedString);
	}
  
  @Test
  public void actorModelRunnerIsConfigurable() {
		Model model = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).systemPublish(publishEnteredTextAsString())
		.build();
	
		customer.withBehavior(model);		
		ModelRunner	customerRunner = customer.getModelRunner();
		
		customerRunner.publishWith(msg -> {
			publishedString = (String)msg;
		});
		
		publishedString = null;
		customer.reactTo(entersText());
		assertEquals(TEXT, publishedString);
  }
  
  @Test
  public void twoActorsInteract() {
		Model targetBehavior = Model.builder()
			.step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).system(displaysEnteredText())
		.build();
		targetActor.withBehavior(targetBehavior);
		
		Model sourceBehavior = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).systemPublish(et -> et).to(targetActor)
		.build();
		sourceActor.withBehavior(sourceBehavior).reactTo(entersText());

		Optional<Step> latestStepRun = targetActor.getModelRunner().getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
  }
  
  @Test
  public void twoActorsInteractWhenSourceActorIsSpecifiedInTargetStepAs() {
    Actor sourceActorClone = new Actor(sourceActor.getName());
    
    Model targetBehavior = Model.builder()
      .useCase(USE_CASE).basicFlow()
        .step(CUSTOMER_ENTERS_TEXT).as(sourceActorClone).user(EntersText.class).system(displaysEnteredText())
    .build();
    targetActor.withBehavior(targetBehavior);
    
    Model sourceBehavior = modelBuilder
      .step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).systemPublish(et -> et).to(targetActor)
    .build();
    sourceActor.withBehavior(sourceBehavior).reactTo(entersText());

    Optional<Step> latestStepRun = targetActor.getModelRunner().getLatestStep();
    assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
  }
  
  @Test
  public void actorReactsToFulfilledCondition() {
    Condition customerHasNotRunAnyStep = () -> !customer.getModelRunner().getLatestStep().isPresent();
    
    Model model = modelBuilder
      .condition(customerHasNotRunAnyStep).system(displaysConstantText())
    .build();
  
    customer.withBehavior(model).run();
    assertNotNull(customer.behavior());

    Optional<Step> latestStepRun = customer.getModelRunner().getLatestStep();
    assertTrue(latestStepRun.isPresent());
  }
  
  @Test
  public void customActorReactsToFulfilledCondition() {
    AbstractActor customActor = new CustomActor();    
    assertNotNull(customActor.behavior());

    customActor.run();
    
    Optional<Step> latestStepRun = customActor.getModelRunner().getLatestStep();
    assertTrue(latestStepRun.isPresent());
  }
  
  private class CustomActor extends AbstractActor{
    @Override
    public Model behavior() {
      Condition actorHasNotRunAnyStep = () -> !getModelRunner().getLatestStep().isPresent();
      
      Model model = Model.builder()
        .condition(actorHasNotRunAnyStep).system(displaysConstantText())
      .build();
      
      return model;
    }
  }
  
  @Test
  public void customActorDoesntReactToFulfilledConditionIfRunHasNotBeenCalled() {
    AbstractActor customActor = new CustomActor();    
    assertNotNull(customActor.behavior());

    Optional<Step> latestStepRun = customActor.getModelRunner().getLatestStep();
    assertFalse(latestStepRun.isPresent());
  }
  
  @Test
  public void twoCustomActorsInteract() {
    Partner2 partner2 = new Partner2();    
    AbstractActor partner1 = new Partner1(partner2);  
    partner2.replyTo(partner1);

    Optional<String> partner2Response = partner1.reactTo(entersText());

    assertEquals(TEXT.toUpperCase(), partner2Response.get());
  }
  
  private class Partner1 extends AbstractActor{
    private AbstractActor partner2;

    public Partner1(AbstractActor partner2) {
      this.partner2 = partner2;
    }
    
    @Override
    public Model behavior() {      
      Model model = Model.builder()
        .useCase(USE_CASE)
          .basicFlow()
            .step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).systemPublish(et -> et).to(partner2)
            .step(SYSTEM_DISPLAYS_TEXT).on(TransformedText.class).systemPublish(tt -> tt.getText())
      .build();
      
      return model;
    }
  }
  
  private class Partner2 extends AbstractActor{    
    private AbstractActor partner1;
    
    @Override
    public Model behavior() {      
      Model model = Model.builder()
        .on(EntersText.class).systemPublish(t -> new TransformedText(t.value().toUpperCase())).to(partner1)
      .build();
      
      return model;
    }
    
    public void replyTo(AbstractActor partner1) {
      this.partner1 = partner1;
    }
  }
  
  private class TransformedText{
    private String text;
    
    public TransformedText(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }
  }
}
