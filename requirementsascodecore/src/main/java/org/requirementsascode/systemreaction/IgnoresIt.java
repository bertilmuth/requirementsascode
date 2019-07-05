package org.requirementsascode.systemreaction;

import java.io.Serializable;
import java.util.function.Consumer;

public class IgnoresIt<T> implements Consumer<T>, Serializable {
	private static final long serialVersionUID = 2735467041247660436L;

	@Override
	public void accept(T command) {
	}
}