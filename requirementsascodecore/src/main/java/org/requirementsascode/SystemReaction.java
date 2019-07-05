package org.requirementsascode;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SystemReaction<T> implements Function<T, Object> {
	private Object system;
	private Function<T, Object> internalFunction;

	SystemReaction(Consumer<T> system) {
		this.system = Objects.requireNonNull(system);
		
		Function<T, Object> nonPublishingReaction = message -> {
			system.accept(message);
			return null;
		};
		this.internalFunction = nonPublishingReaction;
	}
	
	SystemReaction(Consumer<T> system, Function<T, Object> publish) {
		this.system = Objects.requireNonNull(system);
		
		Function<T, Object> nonPublishingReaction = message -> {
			system.accept(message);
			Object returnedEvent = publish.apply(message);
			return returnedEvent;
		};
		this.internalFunction = nonPublishingReaction;
	}

	SystemReaction(Runnable system) {
		this((Consumer<T>) ignoredRunner -> system.run());
		this.system = system;
	}

	SystemReaction(Supplier<Object> system) {
		this.system = Objects.requireNonNull(system);
		
		Function<T, Object> publishingReaction = (Function<T, Object>) message -> system.get();
		this.internalFunction = publishingReaction;
	}

	SystemReaction(Function<T, Object> modelObject) {
		Objects.requireNonNull(modelObject);
		this.system = modelObject;
		this.internalFunction = modelObject;
	}

	public Object getModelObject() {
		return system; 
	}

	@Override
	public Object apply(T event) {
		return internalFunction.apply(event);
	}
}