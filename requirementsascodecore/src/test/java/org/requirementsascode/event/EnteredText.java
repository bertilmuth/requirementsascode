package org.requirementsascode.event;
public class EnteredText{
	private String text;
	
	public EnteredText() {
	}
	
	public EnteredText(String text) {
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