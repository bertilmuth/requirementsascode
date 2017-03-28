package org.requirementsascode.testutil;
public class EnterNumber{
	private Integer value;
	
	public EnterNumber(int value) {
		this.value = value;
	}
	
	public Integer value(){
		return value;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}