package shoppingappjavafx.domain;

import java.util.ArrayList;
import java.util.List;

public class ShippingInformation {
	private String name;
	private String street;
	private String zip;
	private String city;
	private String state;
	private String country;
	
	public ShippingInformation() {
	}

	public String getName() {
		return name;
	}

	public String getStreet() {
		return street;
	}
	
	public String getZip() {
		return zip;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getCountry() {
		return country;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
	public void setZip(String zip) {
		this.zip = zip;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		String str = String.join("\n", getSummary());
		return str;
	}
	
	public List<String> getSummary() {		
		List<String> shippingSummary = new ArrayList<>();
		shippingSummary.add("Name: " + name);
		shippingSummary.add("Street: " + street);
		shippingSummary.add("Zip: " + zip);
		shippingSummary.add("City: " + city);
		shippingSummary.add("State: " + state);
		shippingSummary.add("Country: " + country);				

		return shippingSummary;
	}
}
