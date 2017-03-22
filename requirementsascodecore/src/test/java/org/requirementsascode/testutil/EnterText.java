package org.requirementsascode.testutil;
public class EnterText{
	private String text;
	
	public EnterText() {
	}
	
	public EnterText(String text) {
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