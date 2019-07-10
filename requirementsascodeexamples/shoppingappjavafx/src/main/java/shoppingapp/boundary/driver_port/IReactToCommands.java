package shoppingapp.boundary.driver_port;

public interface IReactToCommands {
	void reactTo(Object... commandObjects);
	boolean canReactTo(Class<? extends Object> messageClass);
}
