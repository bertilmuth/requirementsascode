package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

public class LogException implements Consumer<Throwable> {
	public LogException() {
	}

	@Override
	public void accept(Throwable t) {
		t.printStackTrace();
	}
}
