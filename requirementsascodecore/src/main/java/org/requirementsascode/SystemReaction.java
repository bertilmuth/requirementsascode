package org.requirementsascode;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An instance of this class represents a system reaction as a function, even if it has been specified as
 * a Consumer or Runnable by the user of the library. This approach simplifies development of the rest of the API.
 * 
 * An instance of this class also allows access to the original object (i.e. Consumer, Runnable, or Function)
 * specified via .system(..) or .systemPublish() by the user of the library.
 * That element is called modelObject.
 * 
 * @author b_muth
 *
 * @param <T> the kind of message that is the input for this system reactions
 */
public class SystemReaction<T> implements Function<T, Object> {
	private Object modelObject;
	private Function<? super T, ?> internalFunction;

	public SystemReaction(Consumer<? super T> modelObject) {
		this.modelObject = Objects.requireNonNull(modelObject);
		
		Function<? super T, Object> nonPublishingReaction = message -> {
			modelObject.accept(message);
			return null;
		};
		this.internalFunction = nonPublishingReaction;
	}

	public SystemReaction(Runnable modelObject) {
		this((Consumer<? super T>) ignoredRunner -> modelObject.run());
		this.modelObject = modelObject;
	}

	public SystemReaction(Supplier<? super T> modelObject) {
		this.modelObject = Objects.requireNonNull(modelObject);
		
		Function<? super T, Object> publishingReaction = (Function<? super T, Object>) message -> modelObject.get();
		this.internalFunction = publishingReaction;
	}

	public SystemReaction(Function<? super T, ?> modelObject) {
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