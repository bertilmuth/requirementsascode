package shoppingfxexample.gui.controller;

import org.requirementsascode.UseCaseRunner;

import com.dooapp.fxform.FXForm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import shoppingfxexample.domain.ShippingInformation;
import shoppingfxexample.usecase.event.CheckoutPurchase;
import shoppingfxexample.usecase.event.EnterShippingInformation;

public class EnterShippingInformationController {
	
    @FXML
    private VBox vBox;
	
    @FXML
    private Button confirmButton;

	private ShippingInformation shippingInformation;
	private UseCaseRunner useCaseModelRun;
    
    @FXML
    void onConfirm(ActionEvent event) {
    	EnterShippingInformation enterShippingInformation =  new EnterShippingInformation(shippingInformation);
    	useCaseModelRun.reactTo(enterShippingInformation);
    }
    
	public void enterShippingInformation(CheckoutPurchase checkoutPurchase) {
    	this.shippingInformation = new ShippingInformation();
    	
    	FXForm<ShippingInformation> shippingInformationForm = 
    			new FXForm<>(shippingInformation);
    	vBox.getChildren().add(shippingInformationForm);
	}
	
	public void setUseCaseModelRun(UseCaseRunner useCaseModelRun) {
		this.useCaseModelRun = useCaseModelRun;
	}
}