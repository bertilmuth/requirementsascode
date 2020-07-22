package actor.message;

public class EnterName {
	private String userName;

	public EnterName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}
