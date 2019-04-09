package org.requirementsascode;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SystemReaction<T> implements Function<T, Object[]>{
    private Object modelObject;
    private Function<T, Object[]> internalFunction;
    
    SystemReaction(Consumer<T> modelObject) {
	Objects.requireNonNull(modelObject);
	Function<T, Object[]> nonPublishingReaction = message -> {
	    modelObject.accept(message);
	    return new Object[] {};
	};
	this.modelObject = modelObject;
	this.internalFunction = nonPublishingReaction;
    }
    
    SystemReaction(Consumer<T> modelObject, Function<T, Object[]> returnEventsForMessage) {
	Objects.requireNonNull(modelObject);
	Function<T, Object[]> nonPublishingReaction = message -> {
	    modelObject.accept(message);
	    Object[] returnedEvents = returnEventsForMessage.apply(message);
	    return returnedEvents;
	};
	this.modelObject = modelObject;
	this.internalFunction = nonPublishingReaction;
    }
    
    
    SystemReaction(Runnable modelObject) {
	this((Consumer<T>)ignoredRunner -> modelObject.run());
	this.modelObject = modelObject;
    }
    
    SystemReaction(Supplier<Object[]> modelObject) {
 	Objects.requireNonNull(modelObject);
 	Function<T, Object[]> publishingReaction = (Function<T, Object[]>)message -> modelObject.get();
 	this.modelObject = modelObject;
 	this.internalFunction = publishingReaction;
     }
    
    SystemReaction(Function<T, Object[]> modelObject) {
	Objects.requireNonNull(modelObject);
	this.modelObject = modelObject;
	this.internalFunction = modelObject;
    }
    
    public Object getModelObject() {
	return modelObject;
    }

    @Override
    public Object[] apply(T event) {
	return internalFunction.apply(event);
    }
}