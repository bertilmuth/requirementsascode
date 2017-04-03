package shoppingappjavafx.usecaserealization.systemreaction;

import java.util.function.Consumer;

public class InformUserAndLogException implements Consumer<Throwable> {
	public InformUserAndLogException() {
	}

	@Override
	public void accept(Throwable t) {
		t.printStackTrace();
	}
}
