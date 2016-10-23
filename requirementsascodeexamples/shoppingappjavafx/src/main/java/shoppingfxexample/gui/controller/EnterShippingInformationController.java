package shoppingfxexample.gui.controller;

import com.dooapp.fxform.FXForm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import shoppingfxexample.domain.ShippingInformation;
import shoppingfxexample.usecase.event.CheckoutPurchaseEvent;
import shoppingfxexample.usecase.event.EnterShippingInformationEvent;

public class EnterShippingInformationController extends AbstractController{
	
    @FXML
    private VBox vBox;
	
    @FXML
    private Button confirmButton;

	private ShippingInformation shippingInformation;
    
    @FXML
    void onConfirm(ActionEvent event) {
    	EnterShippingInformationEvent enterShippingInformation =  new EnterShippingInformationEvent(shippingInformation);
    	getUseCaseRunner().reactTo(enterShippingInformation);
    }
    
	public void enterShippingInformation(CheckoutPurchaseEvent checkoutPurchase) {
    	this.shippingInformation = new ShippingInformation();
    	
    	FXForm<ShippingInformation> shippingInformationForm = 
    			new FXForm<>(shippingInformation);
    	vBox.getChildren().add(shippingInformationForm);
	}
}