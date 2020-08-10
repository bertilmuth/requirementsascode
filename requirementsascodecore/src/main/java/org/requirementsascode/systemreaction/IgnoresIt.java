package org.requirementsascode.systemreaction;

import java.util.function.Consumer;

public class IgnoresIt<T> implements Consumer<T>{
	@Override
	public void accept(T command) {
	}
}