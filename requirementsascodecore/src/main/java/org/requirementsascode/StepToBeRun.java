package org.requirementsascode;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Use an instance of this class if you want to find out the details about the
 * step to be run in a custom message handler, and to trigger the system reaction.
 *
 * @see ModelRunner#handleWith(Consumer)
 * @author b_muth
 */
public class StepToBeRun{
	private Step step; 
	private Object message;
  private Object messageToBePublished;

	StepToBeRun() {
	}

  /**
   * Triggers the system reaction of this step.
   * 
   * @return the message returned by the system reaction that will be published
   *         after the handleWith() method completes.
   */
  public Object run() {
    messageToBePublished = runSystemReactionOfStep();
    return messageToBePublished;
  }
	
	private Object runSystemReactionOfStep() {
		@SuppressWarnings("unchecked")
		Function<Object, Object> systemReactionFunction = (Function<Object, Object>) step.getSystemReaction();
		setMessageToBePublished(systemReactionFunction.apply(message));
		return messageToBePublished;
	}
	
	/**
	 * Returns the message that was returned by the latest system reaction.
	 * 
	 * @return an optional containing the message, or an empty optional if there was none.
	 */
	public Optional<Object> getMessageToBePublished(){
	  return Optional.ofNullable(messageToBePublished);
	}
	
	/**
	 * Alter the message to be published after the handleWith() method completes.
	 * IMPORTANT: Call this method after {@link #run()}.
	 * 
	 * @param messageToBePublished the message to be published
	 */
	public void setMessageToBePublished(Object messageToBePublished) {
	  this.messageToBePublished = messageToBePublished;
	}

	/**
	 * Returns the name of the step whose system reaction is performed when
	 * {@link #run()} is called.
	 *
	 * @return the step name.
	 */
	public String getStepName() {
		return step.getName();
	}

	/**
	 * Returns the precondition that needs to be true to trigger the system reaction
	 * when {@link #run()} is called.
	 *
	 * @return the condition, or an empty optional when no condition was specified.
	 */
	public Optional<? extends Object> getCondition() {
		Optional<? extends Object> optionalCondition = step.getCondition();
		return optionalCondition;
	}

	/**
	 * Returns the message object that will be passed to the system reaction when
	 * {@link #run()} is called.
	 *
	 * @return the message, or an empty optional when no message was specified.
	 */
	public Optional<? extends Object> getMessage() {
		Optional<? extends Object> optionalMessage = null;
		if (message instanceof ModelRunner) {
			optionalMessage = Optional.empty();
		} else {
			optionalMessage = Optional.of(message);
		}
		return optionalMessage;
	}

	/**
	 * Returns the system reaction to be executed when {@link #run()} is called.
	 *
	 * @return the system reaction object, as specified in the model.
	 */
	public Object getSystemReaction() {
		Object systemReactionObject = step.getSystemReaction().getModelObject();
		return systemReactionObject;
	}

	void setupWith(Step useCaseStep, Object message) {
		this.step = useCaseStep;
		this.message = message;
	}
}
