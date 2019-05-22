package shoppingapp.boundary.internal.command_handler;

import java.util.function.Consumer;

public class LogsException implements Consumer<Throwable> {
	public LogsException() {
	}

	@Override
	public void accept(Throwable t) {
		t.printStackTrace();
	}
}
