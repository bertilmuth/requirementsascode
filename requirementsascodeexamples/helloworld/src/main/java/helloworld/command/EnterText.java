package helloworld.command;

public class EnterText {	
	private final String text;
	
	public EnterText(String text) {
		this.text = text;
	}

  public String getText() {
    return text;
  }
}