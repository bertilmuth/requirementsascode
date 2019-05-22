package shoppingapp.boundary.internal.domain;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ShippingInformation {
	private StringProperty name = new SimpleStringProperty();
	private StringProperty street = new SimpleStringProperty();
	private StringProperty zip= new SimpleStringProperty();
	private StringProperty city= new SimpleStringProperty();
	private StringProperty state = new SimpleStringProperty();
	private StringProperty country = new SimpleStringProperty();
	
	private static final String UNKNOWN = "";
	
	public ShippingInformation() {
		setName(UNKNOWN);
		setStreet(UNKNOWN);
		setZip(UNKNOWN);
		setCity(UNKNOWN);
		setState(UNKNOWN);
		setCountry(UNKNOWN);
	}

	public String getName() {
		return name.get();
	}
	
    public StringProperty nameProperty() {return name;}

	public String getStreet() {
		return street.get();
	}
	
    public StringProperty streetProperty() {return street;}

	
	public String getZip() {
		return zip.get();
	}
	
    public StringProperty zipProperty() {return zip;}

	public String getCity() {
		return city.get();
	}
	
    public StringProperty cityProperty() {return city;}

	public String getState() {
		return state.get();
	}

	public StringProperty stateProperty() {return state;}

	public String getCountry() {
		return country.get();
	}
	
	public StringProperty countryProperty() {return country;}
	
	public void setName(String value) {
		name.set(value);
	}

	public void setStreet(String value) {
		street.set(value);
	}

	public void setZip(String value) {
		zip.set(value);
	}

	public void setCity(String value) {
		city.set(value);
	}

	public void setState(String value) {
		state.set(value);
	}

	public void setCountry(String value) {
		country.set(value);
	}

	@Override
	public String toString() {
		String str = String.join("\n", getSummary());
		return str;
	}
	
	public List<String> getSummary() {		
		List<String> shippingSummary = new ArrayList<>();
		shippingSummary.add("Name: " + getName());
		shippingSummary.add("Street: " + getStreet());
		shippingSummary.add("Zip: " + getZip());
		shippingSummary.add("City: " + getCity());
		shippingSummary.add("State: " + getState());
		shippingSummary.add("Country: " + getCountry());				

		return shippingSummary;
	}
}
