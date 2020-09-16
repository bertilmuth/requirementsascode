package org.requirementsascode;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class ActorWithBehaviorTest extends AbstractTestCase{
	
	private String publishedString;
  private RecordingActor recordingCustomer;

	@Before
	public void setup() {
		setupWithRecordingModelRunner();
    this.recordingCustomer = RecordingActor.basedOn(customer);
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
	
		customer.withBehavior(model);    
		recordingCustomer.reactTo(entersText());
  	assertRecordedStepNames(recordingCustomer, CUSTOMER_ENTERS_TEXT);
  }
  
  @Test
  public void actorReactsToTwoEvents() {
		Model model = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).system(displaysEnteredText())
			.step(CUSTOMER_ENTERS_NUMBER).on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		customer.withBehavior(model);
    recordingCustomer.reactTo(entersText());
    assertRecordedStepNames(recordingCustomer, CUSTOMER_ENTERS_TEXT);
	
		recordingCustomer.reactTo(entersNumber());
    assertRecordedStepNames(recordingCustomer, CUSTOMER_ENTERS_TEXT, CUSTOMER_ENTERS_NUMBER);
  }
  
  @Test
  public void actorReactsIfActorInModelMatches() {
  	Actor validActor = new Actor("ValidActor");
  	
		Model model = modelBuilder.useCase(USE_CASE).as(validActor)
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();
	
		customer.withBehavior(model);
    recordingCustomer.reactTo(entersText(), validActor);
    assertRecordedStepNames(recordingCustomer, CUSTOMER_ENTERS_TEXT);
  }
  
  @Test
  public void actorDoesnReactIfActorInModelIsDifferent() {
  	Actor validActor = new Actor("ValidActor");
  	Actor invalidActor = new Actor("InvalidActor");

		Model model = modelBuilder.useCase(USE_CASE).as(validActor)
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();
	
		customer.withBehavior(model);
		recordingCustomer.reactTo(entersText(), invalidActor);
    assertRecordedStepNames(recordingCustomer);
  }
  
  @Test
  public void actorReactsIfActorInModelMatchesForSecondEvent() {
    Actor validActor = new Actor("ValidActor");
    Actor invalidActor = new Actor("InvalidActor");

    Model model = modelBuilder.useCase(USE_CASE).as(validActor)
      .step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
    .build();
  
    customer.withBehavior(model);
    recordingCustomer.reactTo(entersText(), invalidActor);
    recordingCustomer.reactTo(entersText(), validActor);
    assertRecordedStepNames(recordingCustomer, CUSTOMER_ENTERS_TEXT);
  }
  
  @Test
  public void actorReturnsCorrectPublishedEvent() {
		Model model = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).systemPublish(publishEnteredTextAsString())
		.build();

		customer.withBehavior(model);
		Optional<String> optionalString = recordingCustomer.reactTo(entersText());
		
		String publishedString = optionalString.get();
		assertEquals(TEXT, publishedString);
	}
  
  @Test
  public void actorModelRunnerIsConfigurable() {
		Model model = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).systemPublish(publishEnteredTextAsString())
		.build();
	
		customer.withBehavior(model);		
		recordingCustomer.getModelRunner().publishWith(msg -> {
	    publishedString = (String)msg;
		});
		
		publishedString = null;
		recordingCustomer.reactTo(entersText());
		assertEquals(TEXT, publishedString);
  }
  
  @Test
  public void twoActorsInteract() {
		Model targetBehavior = Model.builder()
			.step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).system(displaysEnteredText())
		.build();
		targetActor.withBehavior(targetBehavior);
		RecordingActor recordingTargetActor = RecordingActor.basedOn(targetActor);
		
		Model sourceBehavior = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).systemPublish(et -> et).to(recordingTargetActor)
		.build();
		sourceActor.withBehavior(sourceBehavior).reactTo(entersText());

    assertRecordedStepNames(recordingTargetActor, CUSTOMER_ENTERS_TEXT);
  }
  
  @Test
  public void twoActorsInteractWhenSourceActorIsSpecifiedInTargetStepAs() {
    Actor sourceActorClone = new Actor(sourceActor.getName());
    
    Model targetBehavior = Model.builder()
      .useCase(USE_CASE).basicFlow()
        .step(CUSTOMER_ENTERS_TEXT).as(sourceActorClone).user(EntersText.class).system(displaysEnteredText())
    .build();
    targetActor.withBehavior(targetBehavior);
    RecordingActor recordingTargetActor = RecordingActor.basedOn(targetActor);
    
    Model sourceBehavior = modelBuilder
      .step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).systemPublish(et -> et).to(recordingTargetActor)
    .build();
    sourceActor.withBehavior(sourceBehavior).reactTo(entersText());

    assertRecordedStepNames(recordingTargetActor, CUSTOMER_ENTERS_TEXT);
  }
  
  @Test
  public void actorReactsToFulfilledCondition() {
    Condition customerHasNotRunAnyStep = () -> !customer.getModelRunner().getLatestStep().isPresent();
    
    Model model = modelBuilder
      .condition(customerHasNotRunAnyStep).system(displaysConstantText())
    .build();
  
    customer.withBehavior(model);
    recordingCustomer.run();

    assertRecordedStepNames(recordingCustomer, "S1");
  }
  
  @Test
  public void customActorReactsToFulfilledCondition() {
    AbstractActor customActor = new CustomActor();    

    RecordingActor recordingCustomActor = RecordingActor.basedOn(customActor);
    recordingCustomActor.run();
    
    assertRecordedStepNames(recordingCustomActor, "S1");
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
  
  protected void assertRecordedStepNames(RecordingActor actor, String... expectedStepNames) {
    String[] actualStepNames = actor.getRecordedStepNames();
    assertArrayEquals(expectedStepNames, actualStepNames);
  }
}
