package shoppingfxexample.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import shoppingfxexample.domain.ShippingInformation;
import shoppingfxexample.usecase.event.CheckoutPurchase;
import shoppingfxexample.usecase.event.EnterShippingInformation;

public class EnterShippingInformationController extends AbstractController{
	
    @FXML
    private VBox vBox;
	
    @FXML
    private Button confirmButton;
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextField streetField;
    
    @FXML
    private TextField zipField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField stateField;
    
    @FXML
    private TextField countryField;

	private ShippingInformation shippingInformation;
    
    @FXML
    void onConfirm(ActionEvent event) {
    	initShippingInformationFromForm();
    	EnterShippingInformation enterShippingInformation =  new EnterShippingInformation(shippingInformation);
    	useCaseRunner().reactTo(enterShippingInformation);
    }

	private void initShippingInformationFromForm() {
		shippingInformation.setName(nameField.getText());
    	shippingInformation.setStreet(streetField.getText());
    	shippingInformation.setZip(zipField.getText());
    	shippingInformation.setCity(cityField.getText());
    	shippingInformation.setState(stateField.getText());
    	shippingInformation.setCountry(countryField.getText());
	}
    
	public void enterShippingInformation(CheckoutPurchase checkoutPurchaseEvent) {
    	this.shippingInformation = new ShippingInformation();
	}
}