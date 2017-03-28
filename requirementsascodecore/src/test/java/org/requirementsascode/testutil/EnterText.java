package org.requirementsascode.testutil;
public class EnterText{
	private String value;
	
	public EnterText(String text) {
		this.value = text;
	}
	
	public String value(){
		return value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}