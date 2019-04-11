package org.requirementsascode;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class IncludesTest extends AbstractTestCase{
    protected static final String INCLUDED_USE_CASE = "Included use case";
    protected static final String SYSTEM_INCLUDES_USE_CASE = "Step that includes use case";

    @Before
    public void setup() {
      setupWithRecordingModelRunner();
    }
    
  @Test
  public void includesUseCaseWithBasicFlowAtFirstStep_withCallButNoReturn() {
		Model fishLevelModel = Model.builder()
			.useCase(USE_CASE)
	        	.basicFlow()
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
	    .build();
    
		Model seaLevelModel = Model.builder()
		    .useCase(INCLUDED_USE_CASE)
		    	.basicFlow()
		    		.step(SYSTEM_INCLUDES_USE_CASE).user(EntersNumber.class).systemPublish(en -> new Object[] {en})
		.build();

		ModelRunner fishLevelModelRunner = new ModelRunner().run(fishLevelModel);
		ModelRunner seaLevelModelRunner = new ModelRunner().publishWith(event -> fishLevelModelRunner.reactTo(event));
		seaLevelModelRunner.run(seaLevelModel).reactTo(entersNumber(), entersNumber());

		assertEquals(CUSTOMER_ENTERS_NUMBER, fishLevelModelRunner.getLatestStep().get().getName());
		assertEquals(SYSTEM_INCLUDES_USE_CASE, seaLevelModelRunner.getLatestStep().get().getName());
  }  
    
  @Test
  public void includesUseCaseWithBasicFlowAtFirstStep_withCallAndReturn() {
	  	Model fishLevelModel = Model.builder()
	  		.useCase(INCLUDED_USE_CASE)
				.basicFlow().anytime()
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
		.build();

		Model seaLevelModel = Model.builder()
			.useCase(USE_CASE).basicFlow()
				.step(SYSTEM_INCLUDES_USE_CASE).user(EntersNumber.class).systemPublish(en -> new Object[] { en })
				.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
		.build();

		ModelRunner fishLevelModelRunner = new ModelRunner().run(fishLevelModel);
		ModelRunner seaLevelModelRunner = new ModelRunner().publishWith(event -> fishLevelModelRunner.reactTo(event));
		seaLevelModelRunner.run(seaLevelModel).reactTo(entersNumber(), entersNumber());

		assertEquals(CUSTOMER_ENTERS_NUMBER, fishLevelModelRunner.getLatestStep().get().getName());
		assertEquals(SYSTEM_DISPLAYS_TEXT, seaLevelModelRunner.getLatestStep().get().getName());
	}
  
  @Test
  public void includesUseCaseWithBasicFlowAtFirstStep_withoutEventInIncludingUseCase() {
	  	Model fishLevelModel = Model.builder()
	  		.useCase(INCLUDED_USE_CASE)
				.basicFlow().anytime()
					.step(CUSTOMER_ENTERS_NUMBER).user(EntersNumber.class).system(displaysEnteredNumber())
		.build();

		Model seaLevelModel = Model.builder()
			.useCase(USE_CASE).basicFlow()
				.step(SYSTEM_INCLUDES_USE_CASE).systemPublish(() -> new Object[] { entersNumber() })
				.step(SYSTEM_DISPLAYS_TEXT).system(displaysConstantText())
		.build();

		ModelRunner fishLevelModelRunner = new ModelRunner().run(fishLevelModel);
		ModelRunner seaLevelModelRunner = new ModelRunner().publishWith(event -> fishLevelModelRunner.reactTo(event));
		seaLevelModelRunner.run(seaLevelModel).reactTo(entersNumber(), entersNumber());

		assertEquals(CUSTOMER_ENTERS_NUMBER, fishLevelModelRunner.getLatestStep().get().getName());
		assertEquals(SYSTEM_DISPLAYS_TEXT, seaLevelModelRunner.getLatestStep().get().getName());
  }
}
