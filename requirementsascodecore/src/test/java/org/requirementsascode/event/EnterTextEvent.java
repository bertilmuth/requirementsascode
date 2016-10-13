package org.requirementsascode.event;
public class EnterTextEvent{
	private String text;
	
	public EnterTextEvent() {
	}
	
	public EnterTextEvent(String text) {
		set(text);
	}
	
	public String get(){
		return text;
	}

	public void set(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
}