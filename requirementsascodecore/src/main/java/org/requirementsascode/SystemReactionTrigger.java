package org.requirementsascode;

import java.util.function.Consumer;

/**
 * Class used to trigger a system reaction.
 * 
 * Use an instance of this class only if you want to 
 * adapt the system reaction, in order to call the standard 
 * system reaction from your adapted system reaction.
 * 
 * @author b_muth
 *
 */
public class SystemReactionTrigger {
	private Object event;
	private Consumer<Object> systemReaction;
	
	SystemReactionTrigger() {
	}
	
	/**
	 * The system reaction accepts the event (both passed in earlier).
	 * @see #setupWithEventAndSystemReaction(Object, Consumer)
	 */
	public void trigger(){
		systemReaction.accept(event);
	}
	
	@SuppressWarnings("unchecked")
	<T> void setupWithEventAndSystemReaction(T event, Consumer<T> systemReaction) {
		this.event = event;
		this.systemReaction = (Consumer<Object>) systemReaction;
	}
}
