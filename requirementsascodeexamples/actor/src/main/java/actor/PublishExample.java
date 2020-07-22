package actor;

import java.util.Optional;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

public class PublishExample {
	public static void main(String[] args) {
		new PublishExample().buildAndRunModel();
	}

	private void buildAndRunModel() {
		Model model = Model.builder()
			.on(EnterName.class).systemPublish(this::publishNameAsString) 
			.on(String.class).system(this::displayNameString) 
		.build();		
		
		Optional<Object> userName = new ModelRunner().run(model)
			.reactTo(new EnterName("Joe"));	
		System.out.println("Your name is: " + userName.get() + ".");
	}
	
	private String publishNameAsString(EnterName enterName) {
		return enterName.getUserName();
	}
	
	public void displayNameString(String nameString) {
		System.out.println("Welcome, " + nameString + ".");
	}
	
	class EnterName {
		private String userName;

		public EnterName(String userName) {
			this.userName = userName;
		}

		public String getUserName() {
			return userName;
		}
	}
}
