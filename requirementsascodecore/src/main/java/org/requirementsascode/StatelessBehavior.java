package org.requirementsascode;

import java.util.Objects;
import java.util.Optional;

import org.requirementsascode.exception.InfiniteRepetition;

/**
 * This class represents stateless behavior, that is: behavior occurring without
 * remembering past occurences of the same behavior. It is thus suited to be
 * used as a singleton e.g. in a Spring environment.
 * 
 * @author b_muth
 *
 */
public class StatelessBehavior implements Behavior {
	private final BehaviorModel behaviorModel;
	private final Model model;
	private final Object defaultResponse;

	/**
	 * Creates a behavior with the specified model.
	 * 
	 * @param behaviorModel the model describing the requests/responses of the
	 *                      behavior
	 */
	public StatelessBehavior(BehaviorModel behaviorModel) {
		this.behaviorModel = Objects.requireNonNull(behaviorModel, "behaviorModel must not be null!");
		this.model = Objects.requireNonNull(behaviorModel.model(), "behavior must not be null!");
		this.defaultResponse = behaviorModel.defaultResponse();
	}

	@SuppressWarnings("unchecked")
	public <T> Optional<T> reactTo(Object message) {
		try {
			final ModelRunner runner = newModelRunner().run(model);
			final Optional<T> optionalResponse = runner.reactTo(message);
			final T response = optionalResponse.orElse((T) defaultResponse);

			return Optional.ofNullable(response);
		} catch (InfiniteRepetition e) {
			final String exceptionMessage = createExceptionMessage(message);
			throw new BehaviorException(exceptionMessage, e);
		}
	}

	@Override
	public BehaviorModel behaviorModel() {
		return behaviorModel;
	}

	private ModelRunner newModelRunner() {
		return new ModelRunner();
	}

	private String createExceptionMessage(Object message) {
		final String exceptionMessage = "Request type must be different from all response types "
			+ "(current request type: " + message.getClass().getName() + "), and there mustn't be an always true condition";
		return exceptionMessage;
	}
}
