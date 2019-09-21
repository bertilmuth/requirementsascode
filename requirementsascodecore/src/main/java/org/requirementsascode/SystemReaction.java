package org.requirementsascode;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An instance of this class represents an element specified via .system(..) by the user of the library.
 * That element is called modelObject.
 * 
 * Internally, each system reaction needs to be represented as an function, even if it has been specified as
 * a consumer by the user of the library. This approach simplifies development of the rest of the API.
 * 
 * @author b_muth
 *
 * @param <T> the kind of message that is the input for this system reactions
 */
public class SystemReaction<T> implements Function<T, Object> {
	private Object modelObject;
	private Function<T, Object> internalFunction;

	SystemReaction(Consumer<T> modelObject) {
		this.modelObject = Objects.requireNonNull(modelObject);
		
		Function<T, Object> nonPublishingReaction = message -> {
			modelObject.accept(message);
			return null;
		};
		this.internalFunction = nonPublishingReaction;
	}

	SystemReaction(Runnable modelObject) {
		this((Consumer<T>) ignoredRunner -> modelObject.run());
		this.modelObject = modelObject;
	}

	SystemReaction(Supplier<Object> modelObject) {
		this.modelObject = Objects.requireNonNull(modelObject);
		
		Function<T, Object> publishingReaction = (Function<T, Object>) message -> modelObject.get();
		this.internalFunction = publishingReaction;
	}

	SystemReaction(Function<T, Object> modelObject) {
		Objects.requireNonNull(modelObject);
		this.modelObject = modelObject;
		this.internalFunction = modelObject;
	}

	public Object getModelObject() {
		return modelObject; 
	}

	@Override
	public Object apply(T message) {
		return internalFunction.apply(message);
	}
}