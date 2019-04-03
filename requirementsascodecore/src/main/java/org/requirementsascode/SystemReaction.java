package org.requirementsascode;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class SystemReaction<T> implements Function<T, Object[]>{
    private Object systemReaction;
    private Function<T, Object[]> internalSystemReaction;
    
    SystemReaction(Consumer<T> systemReaction) {
	Objects.requireNonNull(systemReaction);
	Function<T, Object[]> nonPublishingReaction = message -> {
	    systemReaction.accept(message);
	    return new Object[] {};
	};
	this.systemReaction = systemReaction;
	this.internalSystemReaction = nonPublishingReaction;
    }
    
    SystemReaction(Function<T, Object[]> systemReaction) {
	Objects.requireNonNull(systemReaction);
	this.systemReaction = systemReaction;
	this.internalSystemReaction = systemReaction;
    }
    
    public Object getSystemReactionObject() {
	return systemReaction;
    }
    
    Function<T, Object[]> getInternalSystemReaction() {
        return internalSystemReaction;
    }

    @Override
    public Object[] apply(T event) {
	return internalSystemReaction.apply(event);
    }
}