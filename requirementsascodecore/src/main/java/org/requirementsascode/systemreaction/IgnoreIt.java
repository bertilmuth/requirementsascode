package org.requirementsascode.systemreaction;

import java.util.function.Consumer;

public class IgnoreIt<T> implements Consumer<T>{
	@Override
	public void accept(T event) {
	}
}