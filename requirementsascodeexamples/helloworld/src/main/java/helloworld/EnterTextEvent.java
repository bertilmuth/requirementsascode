package helloworld;

class EnterTextEvent {
	
	private String text;
	
	public EnterTextEvent(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
}